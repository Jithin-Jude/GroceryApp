package com.jithin.groceryapp.data.remote.api

import com.jithin.groceryapp.data.remote.dto.ProductResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProductApiService {
    @GET("mock-menu-api/menu.json")
    suspend fun getAllProducts(): Response<ProductResponse>
}