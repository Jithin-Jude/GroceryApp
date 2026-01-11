package com.jithin.groceryapp.domain.model

import androidx.annotation.Keep

@Keep
data class ProductDataModel(
    val categories: List<CategoryModel>
)
@Keep
data class CategoryModel(
    val id: Int,
    val name: String,
    val dishes: List<DishModel>
)
@Keep
data class DishModel(
    val id: Int,
    val name: String,
    val price: Double,
    val currency: String,
    val calories: Int,
    val description: String,
    val imageUrl: String,
    val customizationsAvailable: Boolean,
    val isVeg: Boolean,
    val addons: List<AddonModel>,
    val selectedCount: Int = 0,
)
@Keep
data class AddonModel(
    val id: Int,
    val name: String,
    val price: Double
)