package com.jithin.groceryapp.ui.feature


/*
 * --------------------------------------------------------------------------
 * File: HomeScreenView.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.jithin.groceryapp.R
import com.jithin.groceryapp.domain.Routes
import com.jithin.groceryapp.ui.components.AppDrawerView
import com.jithin.groceryapp.ui.components.CategoryTabsView
import com.jithin.groceryapp.ui.components.EmptyScreenView
import com.jithin.groceryapp.ui.components.LoadingView
import com.jithin.groceryapp.ui.theme.AppBackground
import com.jithin.groceryapp.viewmodel.ProductViewModel
import com.jithin.groceryapp.viewmodel.OnboardingViewModel
import com.jithin.groceryapp.viewmodel.CustomerDataViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenView(
    navController: NavHostController,
    productViewModel: ProductViewModel,
    onboardingViewModel: OnboardingViewModel,
    customerDataViewModel: CustomerDataViewModel,
) {
    val listOfProducts by productViewModel.listOfProducts.observeAsState()
    val productsLoading by productViewModel.productsLoader.observeAsState(false)
    val customer by customerDataViewModel.customer.observeAsState()
    val cartCount by productViewModel.totalCartCount.observeAsState(0)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        customerDataViewModel.loadCustomer()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerView(
                userName = customer?.name ?: "Guest",
                userId = customer?.uid ?: "_",
                profilePhotoUrl = customer?.profilePictureUrl,
                onLogoutClick = {
                    productViewModel.clearCart()
                    onboardingViewModel.logout()
                    navController.navigate(Routes.LoginScreen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }

                }
            )
        }
    ) {
        Scaffold (
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.home)) },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    actions = {
                        Box {
                            IconButton(
                                onClick = {
                                    navController.navigate(
                                        Routes.CartScreen.route
                                    )
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_cart),
                                    contentDescription = "Cart"
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .align(Alignment.TopEnd)
                                    .background(Color.Red, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cartCount.toString(),
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    lineHeight = 12.sp
                                )
                            }
                        }
                    }
                )

            }
        ) { paddingValues ->
            Column(
                Modifier
                    .padding(paddingValues)
                    .background(AppBackground)
                    .fillMaxSize()
            ) {
                Box(
                    Modifier.weight(1f)
                ) {
                    if(productsLoading){
                        LoadingView()
                    } else if(listOfProducts.isNullOrEmpty()){
                        EmptyScreenView()
                    } else {
                        CategoryTabsView(
                            modifier = Modifier.fillMaxSize(),
                            categories = listOfProducts!!,
                            onIncrement = { dish ->
                                productViewModel.incrementDishCount(dish)
                            },
                            onDecrement = { dish ->
                                productViewModel.decrementDishCount(dish)
                            },
                            )
                    }
                }
            }
        }
    }
}