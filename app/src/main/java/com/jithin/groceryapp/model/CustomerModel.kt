package com.jithin.groceryapp.model

data class CustomerModel(
    val uid: String = "",
    val name: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val profilePictureUrl: String? = null
)