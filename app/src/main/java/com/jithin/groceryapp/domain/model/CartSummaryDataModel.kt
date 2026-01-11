package com.jithin.groceryapp.domain.model

data class CartSummaryDataModel(
    val totalDishes: Int,
    val totalItems: Int,
    val totalAmount: Double,
    val currency: String,
)
