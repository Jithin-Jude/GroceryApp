package com.jithin.groceryapp.network


/*
 * --------------------------------------------------------------------------
 * File: ProductRepository.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

import com.jithin.groceryapp.domain.DataState
import com.jithin.groceryapp.model.ProductModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getAllProducts(): Flow<DataState<ProductModel>>
}