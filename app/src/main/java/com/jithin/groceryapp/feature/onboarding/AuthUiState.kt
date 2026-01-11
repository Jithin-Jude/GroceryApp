package com.jithin.groceryapp.feature.onboarding

sealed class AuthUiState {
    object Loading : AuthUiState()
    object LoggedOut : AuthUiState()
    data class AuthError(val message: String) : AuthUiState()
    object OTPRequestError : AuthUiState()
    object ShowVerifyOTPScreen : AuthUiState()
    object NeedsName : AuthUiState()
    object NeedsProfilePicture : AuthUiState()
    object OnboardingComplete : AuthUiState()
}