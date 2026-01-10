package com.jithin.groceryapp.ui.feature


/*
 * --------------------------------------------------------------------------
 * File: AskPhoneNumberScreenView.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.arpitkatiyarprojects.countrypicker.CountryPickerOutlinedTextField
import com.arpitkatiyarprojects.countrypicker.models.CountryDetails
import com.jithin.groceryapp.GroceryAppUtils
import com.jithin.groceryapp.MainActivity
import com.jithin.groceryapp.ui.theme.Typography
import com.jithin.groceryapp.viewmodel.AuthViewModel

@Composable
fun AskPhoneNumberScreenView(
    navController: NavHostController,
    authViewModel: AuthViewModel,
) {
    var phoneNumber by remember { mutableStateOf("") }
    var selectedCountry by remember { mutableStateOf<CountryDetails?>(null) }

    val context = LocalContext.current
    val activity = context as Activity

    Scaffold(
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Enter your phone number",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(24.dp))

            CountryPickerOutlinedTextField(
                mobileNumber = phoneNumber,
                onMobileNumberChange = { newNumber ->
                    phoneNumber = newNumber
                },
                onCountrySelected = { country ->
                    selectedCountry = country
                },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = GroceryAppUtils.isValidPhoneNumber("${selectedCountry?.countryPhoneNumberCode ?: ""}$phoneNumber"),
                onClick = {
                    val fullPhone = "${selectedCountry?.countryPhoneNumberCode ?: ""}$phoneNumber"
                    authViewModel.requestOTP(activity, fullPhone)
                    navController.navigate(MainActivity.Routes.VerifyOtpScreen.route)
                }
            ) {
                Text("Request OTP",
                    color = Color.White,
                    style = Typography.titleLarge,
                )
            }
        }
    }
}
