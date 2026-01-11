package com.jithin.groceryapp.domain.model

import androidx.annotation.Keep

@Keep
data class CartSummaryDataModel(
    val totalDishes: Int,
    val totalItems: Int,
    val totalAmount: Double,
    val currency: String,
)
