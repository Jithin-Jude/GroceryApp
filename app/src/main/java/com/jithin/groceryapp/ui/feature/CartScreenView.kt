package com.jithin.groceryapp.ui.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jithin.groceryapp.MainActivity
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

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.style.TextAlign
import com.jithin.groceryapp.ui.theme.GADeepGreen
import com.jithin.groceryapp.ui.theme.GAGreen

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
        CartSummary(0, 0, 0.0, currency = "")
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    enabled = summary.totalItems > 0,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GADeepGreen
                    ),
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
                    Text("Place Order",
                        color = Color.White,
                        style = Typography.titleLarge,
                        )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    ) { paddingValues ->

        if (showSuccessDialog) {
            OrderSuccessDialog()
        }

        /* ✅ SCROLLING PARENT */
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(AppBackground)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(12.dp)
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column {

                    /* ---------- Summary Header ---------- */
                    Column(modifier = Modifier
                        .padding(8.dp)
                        .background(GADeepGreen, shape = RoundedCornerShape(4.dp))
                    ) {
                        Text(
                            text = "${summary.totalDishes} Dishes • ${summary.totalItems} Items",
                            style = Typography.titleLarge,
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        )
                    }

                    /* ---------- Cart Items ---------- */
                    cartItems.forEach { dish ->
                        CartItemView(
                            dish = dish,
                            onIncrement = {
                                productViewModel.incrementDishCount(dish)
                            },
                            onDecrement = {
                                productViewModel.decrementDishCount(dish)
                            }
                        )
                    }

                    if(cartItems.isNotEmpty()){
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    }

                    /* ---------- Total ---------- */
                    Row {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = "Total Amount",
                            style = Typography.titleLarge
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = "${summary.currency} ${summary.totalAmount}",
                            style = Typography.bodyLarge,
                            color = GAGreen,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}