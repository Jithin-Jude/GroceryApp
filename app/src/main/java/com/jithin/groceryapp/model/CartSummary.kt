package com.jithin.groceryapp.model

data class CartSummary(
    val totalDishes: Int,
    val totalItems: Int,
    val totalAmount: Double,
    val currency: String,
)
