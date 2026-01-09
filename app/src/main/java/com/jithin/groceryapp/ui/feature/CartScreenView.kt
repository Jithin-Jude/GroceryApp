package com.jithin.groceryapp.ui.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.jithin.groceryapp.model.CategoryModel
import com.jithin.groceryapp.ui.theme.AppBackground


/*
 * --------------------------------------------------------------------------
 * File: CartScreenView.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

@Composable
fun CartScreenView(
    navController: NavHostController,
) {

    Scaffold { paddingValues ->
        Box(
            Modifier
                .padding(paddingValues)
                .background(AppBackground)
                .fillMaxSize()
        ) {

        }
    }
}