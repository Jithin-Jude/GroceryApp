package com.jithin.groceryapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("categories")
    val categories: List<Category>
)

data class Category(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("dishes")
    val dishes: List<Dish>
)

data class Dish(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    // API sends price as STRING → safer to keep String
    @SerializedName("price")
    val price: String,

    @SerializedName("currency")
    val currency: String,

    @SerializedName("calories")
    val calories: Int,

    @SerializedName("description")
    val description: String,

    @SerializedName("image_url")
    val imageUrl: String,

    @SerializedName("customizations_available")
    val customizationsAvailable: Boolean,

    @SerializedName("is_veg")
    val isVeg: Boolean,

    @SerializedName("addons")
    val addons: List<Addon>
)

data class Addon(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    // Also STRING in API
    @SerializedName("price")
    val price: String
)