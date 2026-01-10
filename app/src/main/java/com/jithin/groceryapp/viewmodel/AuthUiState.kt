package com.jithin.groceryapp.viewmodel


/*
 * --------------------------------------------------------------------------
 * File: AuthUiState.dart
 * Developer: <Jithin/Jude>
 * Created: 10/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

sealed class AuthUiState {
    object Loading : AuthUiState()
    object LoggedOut : AuthUiState()
    object OTPRequestError : AuthUiState()
    object ShowVerifyOTPScreen : AuthUiState()
    object NeedsName : AuthUiState()
    object NeedsProfilePicture : AuthUiState()
    object Ready : AuthUiState()
}