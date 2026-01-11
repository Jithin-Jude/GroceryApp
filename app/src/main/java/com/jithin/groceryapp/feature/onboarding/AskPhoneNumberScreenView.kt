package com.jithin.groceryapp.feature.onboarding


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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.arpitkatiyarprojects.countrypicker.CountryPickerOutlinedTextField
import com.arpitkatiyarprojects.countrypicker.models.CountryDetails
import com.jithin.groceryapp.core.utils.GroceryAppUtils
import com.jithin.groceryapp.R
import com.jithin.groceryapp.core.ui.theme.Typography

@Composable
fun AskPhoneNumberScreenView(
    navController: NavHostController,
    onboardingViewModel: OnboardingViewModel,
) {
    val context = LocalContext.current
    val activity = context as Activity
    val focusRequester = remember { FocusRequester() }

    var phoneNumber by remember { mutableStateOf("") }
    var selectedCountry by remember { mutableStateOf<CountryDetails?>(null) }
    val otpRequestError by onboardingViewModel.otpRequestError.observeAsState()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

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
                text = stringResource(R.string.enter_your_phone_number),
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
                label = { Text(stringResource(R.string.phone_number)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            )

            Spacer(modifier = Modifier.height(12.dp))

            otpRequestError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = GroceryAppUtils.isValidPhoneNumber("${selectedCountry?.countryPhoneNumberCode ?: ""}$phoneNumber"),
                onClick = {
                    val fullPhone = "${selectedCountry?.countryPhoneNumberCode ?: ""}$phoneNumber"
                    onboardingViewModel.requestOTP(activity, fullPhone)
                }
            ) {
                Text(
                    stringResource(R.string.request_otp),
                    color = Color.White,
                    style = Typography.titleLarge,
                )
            }
        }
    }
}
