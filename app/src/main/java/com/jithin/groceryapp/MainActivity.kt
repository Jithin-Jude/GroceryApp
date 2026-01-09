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
import com.jithin.groceryapp.ui.feature.AskPhoneNumberScreenView
import com.jithin.groceryapp.ui.feature.CartScreenView
import com.jithin.groceryapp.ui.feature.HomeScreenView
import com.jithin.groceryapp.ui.feature.LoginScreenView
import com.jithin.groceryapp.ui.theme.GroceryAppTheme
import com.jithin.groceryapp.viewmodel.AuthViewModel
import com.jithin.groceryapp.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val productViewModel: ProductViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GroceryAppTheme {
                AppRoot(
                    productViewModel = productViewModel,
                    authViewModel = authViewModel
                )
            }
        }
    }

    @Composable
    fun AppRoot(
        productViewModel: ProductViewModel,
        authViewModel: AuthViewModel
    ) {
        val navController = rememberNavController()

        val isLoggedIn by authViewModel.isLoggedIn.observeAsState()
        val products by productViewModel.listOfProducts.observeAsState()
        val loading by productViewModel.loader.observeAsState(false)

        when {
            loading -> {
                LoadingView()
            }

            products.isNullOrEmpty() -> {
                EmptyScreenView(productViewModel)
            }

            isLoggedIn != null -> {
                DashboardView(
                    navController = navController,
                    productViewModel = productViewModel,
                    authViewModel = authViewModel,
                    listOfProducts = products!!,
                    startDestination = if (isLoggedIn == true)
                        Routes.HomeScreen.route
                    else
                        Routes.LoginScreen.route
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
            composable(Routes.HomeScreen.route) {
                HomeScreenView(
                    navController,
                    productViewModel,
                    authViewModel,
                    listOfProducts,
                )
            }
            composable(Routes.CartScreen.route + "/{product_id}") { navBackStack ->
                val selectedProductId = navBackStack.arguments?.getString("product_id")
                selectedProductId?.let { id ->
                    CartScreenView(
                        navController,
                        id,
                        listOfProducts
                    )
                }
            }
        }
    }

    sealed class Routes(val route: String) {
        object LoginScreen : Routes("loginScreen")

        object AskPhoneNumberScreen : Routes("askPhoneNumberScreen")
        object HomeScreen : Routes("homeScreen")
        object CartScreen : Routes("cartScreen")
    }
}