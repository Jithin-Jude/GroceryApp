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
import com.jithin.groceryapp.core.utils.GroceryAppUtils
import com.jithin.groceryapp.data.mapper.ProductEntityNetworkMapper
import com.jithin.groceryapp.domain.repository.ProductRepository
import com.jithin.groceryapp.data.remote.api.ProductApiService
import com.jithin.groceryapp.domain.model.ProductDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productApiService: ProductApiService,
    private val productEntityNetworkMapper: ProductEntityNetworkMapper
) : ProductRepository {
    override suspend fun getAllProducts(): Flow<DataState<ProductDataModel>> = flow {
        try {
            GroceryAppUtils.printLog("DEBUG_GR_API :=> before loading")
            emit(DataState.Loading)
            GroceryAppUtils.printLog("DEBUG_GR_API :=> before call")

            val response = productApiService.getAllProducts()
            GroceryAppUtils.printLog("DEBUG_GR_API :=> after call")

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
            GroceryAppUtils.printLog("DEBUG_GR_API :=> api call Exception: ${e.message}")
            emit(DataState.Error(e))
        }
    }
}