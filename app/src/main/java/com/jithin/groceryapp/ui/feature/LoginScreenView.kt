package com.jithin.groceryapp.ui.feature

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jithin.groceryapp.GroceryAppUtils.capitalizeFirstLetter
import com.jithin.groceryapp.R
import com.jithin.groceryapp.domain.Routes
import com.jithin.groceryapp.ui.components.RoundedCornerButton
import com.jithin.groceryapp.ui.theme.AppBackground
import com.jithin.groceryapp.ui.theme.GABlue
import com.jithin.groceryapp.ui.theme.GAGreen
import com.jithin.groceryapp.domain.AuthUiState
import com.jithin.groceryapp.viewmodel.OnboardingViewModel


/*
 * --------------------------------------------------------------------------
 * File: CartScreenView.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

@Composable
fun LoginScreenView(
    navController: NavHostController,
    onboardingViewModel: OnboardingViewModel,
) {
    val context = LocalContext.current
    val activity = context as Activity


    val snackbarHostState = remember { SnackbarHostState() }
    val authState by onboardingViewModel.authUiState.observeAsState(AuthUiState.Loading)

    LaunchedEffect(authState) {
        if (authState is AuthUiState.AuthError) {
            snackbarHostState.showSnackbar(
                message = (authState as AuthUiState.AuthError).message.capitalizeFirstLetter()
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(AppBackground)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {

                Spacer(modifier = Modifier.weight(1f))

                // ───────────────── TOP SECTION (LOGO) ─────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_logo),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(160.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // ───────────────── BOTTOM SECTION (BUTTONS) ─────────────────
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // GOOGLE LOGIN
                    RoundedCornerButton(
                        text = stringResource(R.string.google),
                        iconRes = R.drawable.ic_google,
                        backgroundColor = GABlue,
                        onClick = {
                            onboardingViewModel.loginWithGoogle(activity)
                        }
                    )


                    // PHONE LOGIN
                    RoundedCornerButton(
                        text = stringResource(R.string.phone),
                        iconRes = R.drawable.ic_phone,
                        backgroundColor = GAGreen,
                        onClick = {
                            navController.navigate(Routes.AskPhoneNumberScreen.route)
                        }
                    )
                }
                Spacer(modifier = Modifier.weight(0.5f))
            }
        }
    }
}