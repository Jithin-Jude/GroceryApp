package com.jithin.groceryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jithin.groceryapp.model.CategoryModel
import com.jithin.groceryapp.ui.components.EmptyScreenView
import com.jithin.groceryapp.ui.components.LoadingView
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
        setContent {}
        observeData()
    }

    private fun observeData() {
        productViewModel.listOfProducts.observe(this) { data ->
            setContent {
                val navController = rememberNavController()
                GroceryAppTheme {
                    if (data.isNullOrEmpty()) {
                        EmptyScreenView(productViewModel)
                    } else {
                        DashboardView(
                            navController,
                            productViewModel,
                            data,
                        )
                    }
                }
            }
        }
        productViewModel.loader.observe(this) {
            if (it) {
                setContent {
                    LoadingView()
                }
            }
        }
    }

    @Composable
    fun DashboardView(
        navController: NavHostController,
        viewModel: ProductViewModel,
        listOfProducts: List<CategoryModel>,
    ) {
        NavHost(navController, startDestination = Routes.LoginScreen.route) {
            composable(Routes.LoginScreen.route) {
                LoginScreenView(
                    navController,
                    authViewModel,
                )
            }
            composable(Routes.HomeScreen.route) {
                HomeScreenView(
                    navController,
                    viewModel,
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
        object HomeScreen : Routes("homeScreen")
        object CartScreen : Routes("cartScreen")
    }
}