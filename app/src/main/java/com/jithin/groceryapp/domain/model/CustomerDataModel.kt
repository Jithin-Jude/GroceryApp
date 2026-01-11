package com.jithin.groceryapp.domain.model

data class CustomerDataModel(
    val uid: String = "",
    val name: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val profilePictureUrl: String? = null
)

fun CustomerDataModel.toNonNullMap(): Map<String, Any> {
    val map = mutableMapOf<String, Any>()

    uid?.let { map["uid"] = it }

    if (!name.isNullOrBlank()) {
        map["name"] = name
    }

    if (!email.isNullOrBlank()) {
        map["email"] = email
    }

    if (!phoneNumber.isNullOrBlank()) {
        map["phoneNumber"] = phoneNumber
    }

    if (!profilePictureUrl.isNullOrBlank()) {
        map["profilePictureUrl"] = profilePictureUrl
    }

    return map
}

