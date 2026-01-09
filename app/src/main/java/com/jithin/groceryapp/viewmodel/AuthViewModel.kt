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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    init {
        checkLoginStatus()
    }

    fun checkLoginStatus() {
        _isLoggedIn.value = authRepository.getLoggedInUserId() != null
    }

    fun loginWithGoogle(activity: Activity) {
        viewModelScope.launch {
            authRepository.signInWithGoogle(activity).collect { result ->
                if (result is DataState.Success) {
                    _isLoggedIn.postValue(true)
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
}