package com.jithin.groceryapp.network

import com.jithin.groceryapp.domain.DataState
import com.jithin.groceryapp.model.CustomerModel
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

    suspend fun addOrUpdateCustomer(
        customer: CustomerModel
    ): Flow<DataState<Unit>>

    suspend fun getCustomerById(
        customerId: String
    ): Flow<DataState<CustomerModel>>
}