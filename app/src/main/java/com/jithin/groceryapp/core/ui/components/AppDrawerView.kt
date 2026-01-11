package com.jithin.groceryapp.core.ui.components


/*
 * --------------------------------------------------------------------------
 * File: AppDrawerView.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jithin.groceryapp.R
import com.jithin.groceryapp.core.ui.theme.GAGreen
import com.jithin.groceryapp.core.ui.theme.Typography

@Composable
fun AppDrawerView(
    userName: String,
    userId: String,
    profilePhotoUrl: String?,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // PROFILE SECTION
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .background(GAGreen),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(64.dp))

                AsyncImage(
                    model = profilePhotoUrl,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = userName,
                    style = Typography.titleLarge,
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "ID: $userId",
                    style = Typography.bodyMedium,
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
            }

        Spacer(modifier = Modifier.height(16.dp))

        // LOGOUT
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        ) {
            NavigationDrawerItem(
                label = { Text(
                    stringResource(R.string.log_out),
                    style = Typography.bodyMedium,
                    ) },
                selected = false,
                onClick = onLogoutClick,
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_logout),
                        contentDescription = "Log out",
                        modifier = Modifier.size(32.dp)
                    )
                }
            )
        }
    }
}