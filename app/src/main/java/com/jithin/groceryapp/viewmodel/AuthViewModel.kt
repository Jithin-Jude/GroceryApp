package com.jithin.groceryapp.viewmodel

import android.app.Activity
import android.net.Uri
import com.jithin.groceryapp.network.AuthRepository
import kotlinx.coroutines.flow.collect


/*
 * --------------------------------------------------------------------------
 * File: AuthViewModel.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

import androidx.lifecycle.*
import com.jithin.groceryapp.domain.DataState
import com.jithin.groceryapp.domain.UploadState
import com.jithin.groceryapp.model.CustomerModel
import com.jithin.groceryapp.network.CustomerDataRepository
import com.jithin.groceryapp.network.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val customerDataRepository: CustomerDataRepository,
    private val storageRepository: StorageRepository,
) : ViewModel() {

    private val _authUiState = MutableLiveData<AuthUiState>()
    val authUiState: LiveData<AuthUiState> = _authUiState

    private val _otpLoading = MutableLiveData<Boolean>()
    val otpLoading: LiveData<Boolean> get() = _otpLoading

    private val _otpError = MutableLiveData<String?>()
    val otpError: LiveData<String?> get() = _otpError

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
                                    _authUiState.postValue(AuthUiState.Ready)
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
                            _otpLoading.postValue(true)
                            _otpError.postValue(null)
                        }

                        is DataState.Success -> {
                            _otpLoading.postValue(false)
                            _verificationId.postValue(result.data)
                        }

                        is DataState.Error -> {
                            _otpLoading.postValue(false)
                            _otpError.postValue(
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
                            _otpLoading.postValue(true)
                            _otpError.postValue(null)
                        }

                        is DataState.Success -> {
                            val user = authRepository.getLoggedInUser()
                            user?.let {
                                saveCustomer(
                                    uid = it.uid,
                                    phoneNumber = it.phoneNumber
                                )
                            }
                            _otpLoading.postValue(false)
                            checkAuthAndCustomerState()
                        }

                        is DataState.Error -> {
                            _otpLoading.postValue(false)
                            _otpError.postValue(
                                result.exception.message ?: "Invalid OTP"
                            )
                        }
                    }
                }
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.signOut().collect()
        }
    }

    private fun saveCustomer(
        uid: String,
        name: String? = null,
        email: String? = null,
        phoneNumber: String? = null
    ) {
        viewModelScope.launch {
            val customer = CustomerModel(
                uid = uid,
                name = name,
                email = email,
                phoneNumber = phoneNumber
            )

            customerDataRepository
                .addOrUpdateCustomer(customer)
                .collect()
        }
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