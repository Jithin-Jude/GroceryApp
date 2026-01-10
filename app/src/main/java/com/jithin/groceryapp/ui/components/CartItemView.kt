package com.jithin.groceryapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jithin.groceryapp.R
import com.jithin.groceryapp.model.DishModel
import com.jithin.groceryapp.ui.theme.GADeepGreen
import com.jithin.groceryapp.ui.theme.GAGreen
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
    val vegIcon = if (dish.isVeg) R.drawable.ic_veg else R.drawable.ic_non_veg

    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column() {
            Spacer(modifier = Modifier.height(4.dp))
            Icon(
                painter = painterResource(id = vegIcon),
                contentDescription = "veg/non-veg",
                modifier = Modifier
                    .size(24.dp),
                tint = Color.Unspecified
            )
        }
        Column(
            modifier = Modifier
                .weight(2f)
                .padding(horizontal = 8.dp)
        ) {
            Text(dish.name,
                style = Typography.titleLarge,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("${dish.currency} ${dish.price}",
                style = Typography.bodyLarge,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = Color.DarkGray,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("${dish.calories} calories",
                style = Typography.bodyLarge,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = Color.DarkGray,
            )
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)) {
            AddToCartButton(
                count = dish.selectedCount,
                onIncrement = onIncrement,
                onDecrement = onDecrement,
                buttonColor = GADeepGreen,
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text("${dish.currency} ${dish.price * dish.selectedCount}",
                style = Typography.bodyLarge,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
