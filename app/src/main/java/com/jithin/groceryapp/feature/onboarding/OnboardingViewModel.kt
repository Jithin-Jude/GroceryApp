package com.jithin.groceryapp.feature.onboarding

import android.app.Activity
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jithin.groceryapp.feature.onboarding.AuthUiState
import com.jithin.groceryapp.core.common.DataState
import com.jithin.groceryapp.core.common.UploadState
import com.jithin.groceryapp.domain.model.CustomerDataModel
import com.jithin.groceryapp.domain.repository.AuthRepository
import com.jithin.groceryapp.domain.repository.CustomerDataRepository
import com.jithin.groceryapp.domain.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val customerDataRepository: CustomerDataRepository,
    private val storageRepository: StorageRepository,
) : ViewModel() {

    private val _authUiState = MutableLiveData<AuthUiState>()
    val authUiState: LiveData<AuthUiState> = _authUiState

    private val _otpVerificationInProgress = MutableLiveData<Boolean>()
    val otpVerificationInProgress: LiveData<Boolean> get() = _otpVerificationInProgress

    private val _otpRequestError = MutableLiveData<String?>()
    val otpRequestError: LiveData<String?> get() = _otpRequestError

    private val _otpVerificationError = MutableLiveData<String?>()
    val otpVerificationError: LiveData<String?> get() = _otpVerificationError

    private val _verificationId = MutableLiveData<String>()
    val verificationId: LiveData<String> get() = _verificationId

    private val _uploadState = MutableLiveData<UploadState>(UploadState.Idle)
    val uploadState: LiveData<UploadState> = _uploadState

    init {
        checkAuthAndCustomerState()
    }

    private fun checkAuthAndCustomerState() {
        val uid = authRepository.getLoggedInUser()?.uid

        if (uid == null) {
            _authUiState.postValue(AuthUiState.LoggedOut)
            return
        }

        viewModelScope.launch {
            customerDataRepository
                .getCustomerById(uid)
                .collect { result ->
                    when (result) {
                        is DataState.Success -> {
                            val customer = result.data
                            when {
                                customer.name.isNullOrBlank() ->
                                    _authUiState.postValue(AuthUiState.NeedsName)

                                customer.profilePictureUrl.isNullOrBlank() ->
                                    _authUiState.postValue(AuthUiState.NeedsProfilePicture)

                                else ->
                                    _authUiState.postValue(AuthUiState.OnboardingComplete)
                            }
                        }

                        is DataState.Error -> {
                            // Customer document missing → treat as incomplete
                            _authUiState.postValue(AuthUiState.NeedsName)
                        }

                        is DataState.Loading -> {
                            _authUiState.postValue(AuthUiState.Loading)
                        }
                    }
                }
        }
    }

    fun loginWithGoogle(activity: Activity) {
        _authUiState.postValue(AuthUiState.Loading)
        viewModelScope.launch {
            authRepository.signInWithGoogle(activity).collect { result ->
                if (result is DataState.Success) {
                    val user = authRepository.getLoggedInUser()
                    user?.let {
                        saveCustomer(
                            uid = it.uid,
                            name = it.displayName,
                            email = it.email
                        )
                    }
                    checkAuthAndCustomerState()
                } else if (result is DataState.Error) {
                    _authUiState.postValue(AuthUiState.AuthError(message = result.exception.message.toString()))
                }
            }
        }
    }

    fun requestOTP(activity: Activity, phoneNumber: String) {
        viewModelScope.launch {
            authRepository
                .requestOTP(activity, phoneNumber)
                .collect { result ->
                    when (result) {
                        is DataState.Loading -> {
                            _authUiState.postValue(AuthUiState.Loading)
                            _otpRequestError.postValue(null)
                        }

                        is DataState.Success -> {
                            _verificationId.postValue(result.data)
                            _authUiState.postValue(AuthUiState.ShowVerifyOTPScreen)
                        }

                        is DataState.Error -> {
                            _authUiState.postValue(AuthUiState.OTPRequestError)
                            _otpRequestError.postValue(
                                result.exception.message ?: "OTP request failed"
                            )
                        }
                    }
                }
        }
    }

    fun verifyOTP(otp: String) {
        val verificationId = _verificationId.value ?: return

        viewModelScope.launch {
            authRepository
                .verifyOTP(verificationId, otp)
                .collect { result ->
                    when (result) {
                        is DataState.Loading -> {
                            _otpVerificationInProgress.postValue(true)
                            _otpVerificationError.postValue(null)
                        }

                        is DataState.Success -> {
                            val user = authRepository.getLoggedInUser()
                            user?.let {
                                saveCustomer(
                                    uid = it.uid,
                                    phoneNumber = it.phoneNumber
                                )
                            }
                            _otpVerificationInProgress.postValue(false)
                            checkAuthAndCustomerState()
                        }

                        is DataState.Error -> {
                            _otpVerificationInProgress.postValue(false)
                            _otpVerificationError.postValue(
                                result.exception.message ?: "Invalid OTP"
                            )
                        }
                    }
                }
        }
    }

    fun logout() {
        _authUiState.postValue(AuthUiState.LoggedOut)
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.signOut().collect()
        }
    }

    private suspend fun saveCustomer(
        uid: String,
        name: String? = null,
        email: String? = null,
        phoneNumber: String? = null
    ) {
        val customer = CustomerDataModel(
            uid = uid,
            name = name,
            email = email,
            phoneNumber = phoneNumber
        )

        customerDataRepository
            .addCustomer(customer)
            .collect()
    }
    fun updateCustomerName(name: String) {
        val uid = authRepository.getLoggedInUser()?.uid ?: return

        viewModelScope.launch {
            customerDataRepository
                .updateCustomerFields(
                    uid = uid,
                    fields = mapOf("name" to name)
                )
                .collect { result ->
                    if (result is DataState.Success) {
                        checkAuthAndCustomerState()
                    }
                }
        }
    }

    fun uploadProfilePicture(imageUri: Uri) {
        val uid = authRepository.getLoggedInUser()?.uid ?: return

        viewModelScope.launch {
            storageRepository
                .uploadProfilePicture(uid, imageUri)
                .collect { state ->
                    _uploadState.postValue(state)

                    if (state is UploadState.Success) {
                        // 🔥 Safe partial update
                        customerDataRepository
                            .updateCustomerFields(
                                uid,
                                mapOf("profilePictureUrl" to state.downloadUrl)
                            )
                            .collect {
                                if (it is DataState.Success) {
                                    checkAuthAndCustomerState()
                                }
                            }
                    }
                }
        }
    }
}