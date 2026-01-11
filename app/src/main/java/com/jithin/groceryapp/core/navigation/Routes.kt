package com.jithin.groceryapp.core.navigation

sealed class Routes(val route: String) {
    object LoginScreen : Routes("loginScreen")
    object AskPhoneNumberScreen : Routes("askPhoneNumberScreen")
    object VerifyOtpScreen : Routes("verifyOtpScreen")
    object AskNameScreen : Routes("askNameScreen")
    object AskProfilePictureScreen : Routes("askProfilePictureScreen")
    object HomeScreen : Routes("homeScreen")
    object CartScreen : Routes("cartScreen")
}