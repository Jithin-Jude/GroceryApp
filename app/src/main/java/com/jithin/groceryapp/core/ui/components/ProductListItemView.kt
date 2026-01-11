package com.jithin.groceryapp.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jithin.groceryapp.core.utils.GroceryAppUtils.networkImageLoaderWithCache
import com.jithin.groceryapp.core.utils.GroceryAppUtils.roundTo2Decimals
import com.jithin.groceryapp.R
import com.jithin.groceryapp.domain.model.DishModel
import com.jithin.groceryapp.core.ui.theme.GAGreen
import com.jithin.groceryapp.core.ui.theme.Typography


/*
 * --------------------------------------------------------------------------
 * File: ProductListItemView.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

@Composable
fun ProductListItemView(dish: DishModel,
                        onIncrement: (dish: DishModel) -> Unit,
                        onDecrement: (dish: DishModel) -> Unit,
                        ) {
    val context = LocalContext.current
    val vegIcon = if (dish.isVeg) R.drawable.ic_veg else R.drawable.ic_non_veg

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Column {
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
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            Text(dish.name,
                style = Typography.titleLarge,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row() {
                Text("${dish.currency} ${dish.price.roundTo2Decimals()}",
                    style = Typography.bodyLarge,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f))
                Text("${dish.calories} calories",
                    style = Typography.bodyLarge,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(dish.description,
                color = Color.Gray,
                style = Typography.bodyMedium,
            )

            Spacer(modifier = Modifier.height(16.dp))
            AddToCartButton(
                count = dish.selectedCount,
                onIncrement = {
                    onIncrement(dish)
                              },
                onDecrement = { if (dish.selectedCount > 0) {
                    onDecrement(dish)
                } },
                buttonColor = GAGreen,
            )

            if(dish.customizationsAvailable){
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    stringResource(R.string.customizations_available),
                    color = Color.Red,
                    style = Typography.bodyMedium,
                    )
            }
        }
        AsyncImage(
            modifier = Modifier
                .width(80.dp)
                .height(100.dp),
            model = dish.imageUrl.networkImageLoaderWithCache(
                context = context,
                R.drawable.ic_placeholder_dish
            ),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )
    }
}