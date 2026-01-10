package com.jithin.groceryapp.viewmodel

import android.app.Activity
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
import com.jithin.groceryapp.model.CustomerModel
import com.jithin.groceryapp.network.CustomerDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val customerDataRepository: CustomerDataRepository
) : ViewModel() {

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    private val _otpLoading = MutableLiveData<Boolean>()
    val otpLoading: LiveData<Boolean> get() = _otpLoading

    private val _otpError = MutableLiveData<String?>()
    val otpError: LiveData<String?> get() = _otpError

    private val _verificationId = MutableLiveData<String>()
    val verificationId: LiveData<String> get() = _verificationId

    init {
        checkLoginStatus()
    }

    fun checkLoginStatus() {
        _isLoggedIn.value = authRepository.getLoggedInUser()?.uid != null
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
                    _isLoggedIn.postValue(true)
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
                            _isLoggedIn.postValue(true)
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
            _isLoggedIn.postValue(false)
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

}