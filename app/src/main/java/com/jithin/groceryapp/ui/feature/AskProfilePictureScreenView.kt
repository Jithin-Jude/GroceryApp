package com.jithin.groceryapp.ui.feature


/*
 * --------------------------------------------------------------------------
 * File: AskProfilePictureScreenView.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.jithin.groceryapp.R
import com.jithin.groceryapp.domain.UploadState
import com.jithin.groceryapp.ui.theme.Typography
import com.jithin.groceryapp.viewmodel.OnboardingViewModel

@Composable
fun AskProfilePictureScreenView(
    navController: NavHostController,
    onboardingViewModel: OnboardingViewModel,
) {
    val uploadState by onboardingViewModel.uploadState.observeAsState(UploadState.Idle)
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
    }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            Button(
                onClick = {
                    imageUri?.let {
                        onboardingViewModel.uploadProfilePicture(it)
                    }
                },
                enabled = imageUri != null && uploadState !is UploadState.Uploading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp)
                    .windowInsetsPadding(WindowInsets.ime)
                    .height(52.dp)
            ) {
                Text(
                    stringResource(R.string.action_continue),
                    color = Color.White,
                    style = Typography.titleLarge,
                    )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(Modifier.height(24.dp))

            Text(
                stringResource(R.string.add_a_profile_picture),
                style = Typography.titleLarge,
                )

            Spacer(Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                when (uploadState) {
                    is UploadState.Uploading,
                    is UploadState.Progress -> {
                        CircularProgressIndicator()
                    }

                    else -> {
                        if (imageUri != null) {
                            AsyncImage(model = imageUri, contentDescription = null, contentScale = ContentScale.Crop)
                        } else {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "Gallery",
                                tint = Color.Black,
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            if (uploadState is UploadState.Progress) {
                Text(
                    stringResource(
                        R.string.label_uploading,
                        (uploadState as UploadState.Progress).percent
                    ))
            }

            Spacer(Modifier.weight(1f))

        }
    }
}
