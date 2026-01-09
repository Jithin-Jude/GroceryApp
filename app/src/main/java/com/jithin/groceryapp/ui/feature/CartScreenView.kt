package com.jithin.groceryapp.ui.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.jithin.groceryapp.MainActivity
import com.jithin.groceryapp.R
import com.jithin.groceryapp.model.CartSummary
import com.jithin.groceryapp.ui.components.CartItemView
import com.jithin.groceryapp.ui.dialog.OrderSuccessDialog
import com.jithin.groceryapp.ui.theme.AppBackground
import com.jithin.groceryapp.viewmodel.ProductViewModel
import com.jithin.groceryapp.ui.theme.Typography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/*
 * --------------------------------------------------------------------------
 * File: CartScreenView.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreenView(
    navController: NavHostController,
    productViewModel: ProductViewModel,
) {
    var showSuccessDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val cartItems by productViewModel.cartItems.observeAsState(emptyList())
    val summary by productViewModel.cartSummary.observeAsState(
        CartSummary(0, 0, 0.0)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Summary") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = summary.totalItems > 0,
                    onClick = {
                        showSuccessDialog = true

                        scope.launch {
                            delay(3000)

                            productViewModel.clearCart()

                            showSuccessDialog = false

                            navController.navigate(MainActivity.Routes.HomeScreen.route) {
                                popUpTo(MainActivity.Routes.HomeScreen.route) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                ) {
                    Text("Place Order • ₹${summary.totalAmount}")
                }
            }
        }
    ) { paddingValues ->
        if (showSuccessDialog) {
            OrderSuccessDialog()
        }

        Column(
            Modifier
                .padding(paddingValues)
                .background(AppBackground)
                .fillMaxSize()
        ) {

            // 🔹 Summary header
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = "${summary.totalDishes} Dishes • ${summary.totalItems} Items",
                    style = Typography.titleLarge
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Total: ₹${summary.totalAmount}",
                    style = Typography.titleLarge
                )
            }

            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )

            // 🔹 Cart items list
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(cartItems.size) { index ->
                    val dish = cartItems[index]
                    CartItemView(
                        dish = cartItems[index],
                        onIncrement = {
                            productViewModel.incrementDishCount(dish)
                        },
                        onDecrement = {
                            productViewModel.decrementDishCount(dish)
                        }
                    )
                }
            }
        }
    }
}