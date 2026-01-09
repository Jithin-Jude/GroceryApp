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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.jithin.groceryapp.GroceryAppUtils.networkImageLoaderWithCache
import com.jithin.groceryapp.MainActivity
import com.jithin.groceryapp.R
import com.jithin.groceryapp.model.CategoryModel
import com.jithin.groceryapp.model.DishModel
import com.jithin.groceryapp.model.ProductModel
import com.jithin.groceryapp.ui.theme.AppBackground
import com.jithin.groceryapp.ui.theme.DividerGrey
import com.jithin.groceryapp.viewmodel.ProductViewModel
import com.jithin.groceryapp.ui.theme.Typography


//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    val navController = rememberNavController()
//    DukaanDostTheme {
//        HomeScreenView(navController, false, emptyList(), emptyList())
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenView(
    navController: NavHostController,
    viewModel: ProductViewModel,
    listOfProducts: List<CategoryModel>,
) {

    Scaffold { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .background(AppBackground)
                .fillMaxSize()
        ) {
            Box(
                Modifier
                    .weight(1f)) {
                ProductListView(Modifier.fillMaxSize(), listOfProducts, onClickProduct = {
                    navController.navigate(MainActivity.Routes.CartScreen.route)
                })
            }

        }
    }
}

@Composable
fun ProductListView(
    modifier: Modifier,
    listOfProducts: List<CategoryModel>,
    onClickProduct: (product: DishModel) -> Unit
) {
    LazyColumn(modifier) {
        items(listOfProducts) { product ->
            ProductListItemView(product.dishes.first(), onClickProduct)
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(DividerGrey))
        }
    }
}

@Composable
fun ProductListItemView(product: DishModel, onClickProduct: (product: DishModel) -> Unit) {
    val context = LocalContext.current

    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() } // This is mandatory
            ) {
                onClickProduct(product)
            }) {
        AsyncImage(
            modifier = Modifier.size(100.dp),
            model = product.imageUrl.networkImageLoaderWithCache(context = context, R.drawable.ic_placeholed_shopping_bag),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = product.name,
                style = Typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = product.price.toString(),
                style = Typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}