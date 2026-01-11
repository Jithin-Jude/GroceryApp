package com.jithin.groceryapp.domain.repository


/*
 * --------------------------------------------------------------------------
 * File: ProductRepository.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

import com.jithin.groceryapp.core.common.DataState
import com.jithin.groceryapp.domain.model.ProductDataModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getAllProducts(): Flow<DataState<ProductDataModel>>
}