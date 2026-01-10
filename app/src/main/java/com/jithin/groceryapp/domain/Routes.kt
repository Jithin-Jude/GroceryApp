package com.jithin.groceryapp.domain


/*
 * --------------------------------------------------------------------------
 * File: Routes.dart
 * Developer: <Jithin/Jude>
 * Created: 10/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

sealed class Routes(val route: String) {
    object LoginScreen : Routes("loginScreen")
    object AskPhoneNumberScreen : Routes("askPhoneNumberScreen")
    object VerifyOtpScreen : Routes("verifyOtpScreen")
    object AskNameScreen : Routes("askNameScreen")
    object AskProfilePictureScreen : Routes("askProfilePictureScreen")
    object HomeScreen : Routes("homeScreen")
    object CartScreen : Routes("cartScreen")
}