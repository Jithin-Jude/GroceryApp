package com.jithin.groceryapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jithin.groceryapp.model.DishModel
import com.jithin.groceryapp.ui.theme.Typography


/*
 * --------------------------------------------------------------------------
 * File: CartItemView.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

@Composable
fun CartItemView(
    dish: DishModel,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(dish.name, style = Typography.titleMedium)
            Text(
                "₹${dish.price} x ${dish.selectedCount}",
                style = Typography.bodyMedium,
                color = Color.Gray
            )
        }

        AddToCartButton(
            count = dish.selectedCount,
            onIncrement = onIncrement,
            onDecrement = onDecrement
        )
    }
}
