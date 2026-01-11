package com.jithin.groceryapp.data.repository


/*
 * --------------------------------------------------------------------------
 * File: ProductRepositoryImpl.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

import com.jithin.groceryapp.core.common.DataState
import com.jithin.groceryapp.data.mapper.ProductEntityNetworkMapper
import com.jithin.groceryapp.domain.repository.ProductRepository
import com.jithin.groceryapp.data.remote.api.ProductApiService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productApiService: ProductApiService,
    private val productEntityNetworkMapper: ProductEntityNetworkMapper
) : ProductRepository {
    override suspend fun getAllProducts() = flow {
        try {
            emit(DataState.Loading)

            val response = productApiService.getAllProducts()

            if (response.isSuccessful) {
                response.body()?.let { productResponse ->

                    // SINGLE object → SINGLE model
                    val productModel =
                        productEntityNetworkMapper.mapFromEntity(productResponse)

                    emit(DataState.Success(productModel))
                } ?: emit(DataState.Error(Exception("Empty response")))
            } else {
                emit(DataState.Error(Exception(response.message())))
            }

        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}