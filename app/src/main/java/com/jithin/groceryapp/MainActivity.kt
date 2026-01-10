package com.jithin.groceryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jithin.groceryapp.model.CategoryModel
import com.jithin.groceryapp.ui.components.EmptyScreenView
import com.jithin.groceryapp.ui.components.LoadingView
import com.jithin.groceryapp.ui.feature.AskNameScreenView
import com.jithin.groceryapp.ui.feature.AskPhoneNumberScreenView
import com.jithin.groceryapp.ui.feature.CartScreenView
import com.jithin.groceryapp.ui.feature.HomeScreenView
import com.jithin.groceryapp.ui.feature.LoginScreenView
import com.jithin.groceryapp.ui.feature.VerifyOTPScreen
import com.jithin.groceryapp.ui.theme.GroceryAppTheme
import com.jithin.groceryapp.viewmodel.AuthUiState
import com.jithin.groceryapp.viewmodel.AuthViewModel
import com.jithin.groceryapp.viewmodel.CustomerDataViewModel
import com.jithin.groceryapp.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val productViewModel: ProductViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private val customerDataViewModel: CustomerDataViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GroceryAppTheme {
                AppRoot(
                    productViewModel = productViewModel,
                    authViewModel = authViewModel,
                    customerDataViewModel = customerDataViewModel,
                )
            }
        }
    }

    @Composable
    fun AppRoot(
        productViewModel: ProductViewModel,
        authViewModel: AuthViewModel,
        customerDataViewModel: CustomerDataViewModel,
    ) {
        val navController = rememberNavController()

        val authState by authViewModel.authUiState.observeAsState(AuthUiState.Loading)
        val products by productViewModel.listOfProducts.observeAsState()
        val loading by productViewModel.loader.observeAsState(false)

        when {
            loading || authState is AuthUiState.Loading -> {
                LoadingView()
            }

            products.isNullOrEmpty() -> {
                EmptyScreenView(productViewModel)
            }

            else -> {
                val startDestination = when (authState) {
                    AuthUiState.LoggedOut -> Routes.LoginScreen.route
                    AuthUiState.NeedsName -> Routes.AskNameScreen.route
                    AuthUiState.Ready -> Routes.HomeScreen.route
                    AuthUiState.Loading -> Routes.LoginScreen.route // fallback
                }

                DashboardView(
                    navController = navController,
                    startDestination = startDestination,
                    productViewModel = productViewModel,
                    authViewModel = authViewModel,
                    customerDataViewModel = customerDataViewModel,
                    listOfProducts = products!!
                )
            }
        }
    }

    @Composable
    fun DashboardView(
        navController: NavHostController,
        startDestination: String,
        productViewModel: ProductViewModel,
        authViewModel: AuthViewModel,
        customerDataViewModel: CustomerDataViewModel,
        listOfProducts: List<CategoryModel>,
    ) {
        NavHost(navController, startDestination = startDestination) {
            composable(Routes.LoginScreen.route) {
                LoginScreenView(
                    navController,
                    authViewModel,
                )
            }
            composable(Routes.AskPhoneNumberScreen.route) {
                AskPhoneNumberScreenView(
                    navController,
                    authViewModel,
                )
            }
            composable(Routes.VerifyOtpScreen.route) {
                VerifyOTPScreen(
                    navController,
                    authViewModel,
                )
            }
            composable(Routes.AskNameScreen.route) {
                AskNameScreenView(
                    navController,
                    authViewModel,
                )
            }
            composable(Routes.HomeScreen.route) {
                HomeScreenView(
                    navController = navController,
                    productViewModel = productViewModel,
                    authViewModel = authViewModel,
                    customerDataViewModel = customerDataViewModel,
                    listOfProducts = listOfProducts,
                )
            }
            composable(Routes.CartScreen.route) { navBackStack ->
                CartScreenView(
                    navController,
                    productViewModel,
                )
            }
        }
    }

    sealed class Routes(val route: String) {
        object LoginScreen : Routes("loginScreen")
        object AskPhoneNumberScreen : Routes("askPhoneNumberScreen")
        object VerifyOtpScreen : Routes("verifyOtpScreen")
        object AskNameScreen : Routes("askNameScreen")
        object HomeScreen : Routes("homeScreen")
        object CartScreen : Routes("cartScreen")
    }
}