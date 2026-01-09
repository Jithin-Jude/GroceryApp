package com.jithin.groceryapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jithin.groceryapp.GroceryAppUtils.networkImageLoaderWithCache
import com.jithin.groceryapp.R
import com.jithin.groceryapp.model.DishModel
import com.jithin.groceryapp.ui.theme.Typography


/*
 * --------------------------------------------------------------------------
 * File: ProductListItemView.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

@Composable
fun ProductListItemView(product: DishModel, onClickProduct: (product: DishModel) -> Unit) {
    val context = LocalContext.current
    val vegIcon = if (product.isVeg) R.drawable.ic_veg else R.drawable.ic_non_veg

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() } // This is mandatory
            ) {
                onClickProduct(product)
            }) {
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
            Text(product.name,
                style = Typography.titleLarge,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row() {
                Text("${product.currency} ${product.price}",
                    style = Typography.bodyLarge,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f))
                Text("${product.calories} calories",
                    style = Typography.bodyLarge,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(product.description,
                color = Color.Gray,
                style = Typography.bodyMedium,
            )

            if(product.customizationsAvailable){
                Spacer(modifier = Modifier.height(16.dp))
                Text("Customizations Available",
                    color = Color.Red,
                    style = Typography.bodyMedium,
                    )
            }
        }
        AsyncImage(
            modifier = Modifier
                .width(80.dp)
                .height(100.dp),
            model = product.imageUrl.networkImageLoaderWithCache(
                context = context,
                R.drawable.ic_placeholed_shopping_bag
            ),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )
    }
}