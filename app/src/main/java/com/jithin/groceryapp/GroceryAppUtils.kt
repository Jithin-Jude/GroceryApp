package com.jithin.groceryapp

import android.content.Context
import android.util.Log
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers

object GroceryAppUtils {
    fun printLog(text: String?) {
        Log.d("GROCERY_APP", "DEBUG :=> ${text}")
    }

    fun String.networkImageLoaderWithCache(context: Context, placeholder: Int): ImageRequest {
        return ImageRequest.Builder(context)
            .data(this)
            .dispatcher(Dispatchers.IO)
            .memoryCacheKey(this)
            .diskCacheKey(this)
            .placeholder(placeholder)
            .error(placeholder)
            .fallback(placeholder)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    }
}