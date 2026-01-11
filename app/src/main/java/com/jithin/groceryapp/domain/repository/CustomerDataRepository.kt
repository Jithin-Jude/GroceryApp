package com.jithin.groceryapp.domain.repository

import com.jithin.groceryapp.core.common.DataState
import com.jithin.groceryapp.domain.model.CustomerDataModel
import kotlinx.coroutines.flow.Flow


/*
 * --------------------------------------------------------------------------
 * File: CustomerDataRepository.dart
 * Developer: <Jithin/Jude>
 * Created: 10/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */


interface CustomerDataRepository {

    suspend fun addCustomer(
        customer: CustomerDataModel
    ): Flow<DataState<Unit>>

    suspend fun getCustomerById(
        customerId: String
    ): Flow<DataState<CustomerDataModel>>

    suspend fun updateCustomerFields(
        uid: String,
        fields: Map<String, Any>
    ): Flow<DataState<Unit>>
}