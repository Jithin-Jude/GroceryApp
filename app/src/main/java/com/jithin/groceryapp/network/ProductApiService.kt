package com.jithin.groceryapp.network


/*
 * --------------------------------------------------------------------------
 * File: ProductApiService.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */
import retrofit2.Response
import retrofit2.http.GET

interface ProductApiService {
    @GET("mock-menu-api/menu.json")
    suspend fun getAllProducts(): Response<ProductResponse>
}