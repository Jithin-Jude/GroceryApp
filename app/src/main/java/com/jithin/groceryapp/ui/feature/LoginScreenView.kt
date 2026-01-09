package com.jithin.groceryapp.ui.feature

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jithin.groceryapp.MainActivity
import com.jithin.groceryapp.R
import com.jithin.groceryapp.ui.components.RoundedCornerButton
import com.jithin.groceryapp.ui.theme.AppBackground
import com.jithin.groceryapp.viewmodel.AuthViewModel


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
    authViewModel: AuthViewModel,
) {
    val context = LocalContext.current
    val activity = context as Activity

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(AppBackground)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {

                // ───────────────── TOP SECTION (LOGO) ─────────────────
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_logo),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(160.dp)
                    )
                }

                // ───────────────── BOTTOM SECTION (BUTTONS) ─────────────────
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // GOOGLE LOGIN
                    RoundedCornerButton(
                        text = "Google",
                        iconRes = R.drawable.ic_google,
                        backgroundColor = Color.Blue,
                        onClick = {
                            authViewModel.loginWithGoogle(activity)
                        }
                    )


                    // PHONE LOGIN
                    RoundedCornerButton(
                        text = "Phone",
                        iconRes = R.drawable.ic_phone,
                        backgroundColor = Color.Green,
                        onClick = {
                            navController.navigate(MainActivity.Routes.AskPhoneNumberScreen.route)
                        }
                    )
                }
            }
        }
    }
}