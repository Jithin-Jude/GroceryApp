package com.jithin.groceryapp.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jithin.groceryapp.domain.model.DishModel
import com.jithin.groceryapp.core.ui.theme.DividerGrey


/*
 * --------------------------------------------------------------------------
 * File: ProductListView.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

@Composable
fun ProductListView(
    modifier: Modifier,
    listOfProducts: List<DishModel>,
    onIncrement: (dish: DishModel) -> Unit,
    onDecrement: (dish: DishModel) -> Unit,
) {
    LazyColumn(modifier) {
        items(listOfProducts.size) { index ->
            ProductListItemView(
                dish = listOfProducts[index],
                onIncrement = onIncrement,
                onDecrement = onDecrement,
                )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(DividerGrey)
            )
        }
    }
}
