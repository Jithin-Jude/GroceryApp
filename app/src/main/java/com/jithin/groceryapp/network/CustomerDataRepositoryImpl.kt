package com.jithin.groceryapp.network


/*
 * --------------------------------------------------------------------------
 * File: CustomerDataRepositoryImpl.dart
 * Developer: <Jithin/Jude>
 * Created: 10/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

import com.google.firebase.firestore.FirebaseFirestore
import com.jithin.groceryapp.domain.DataState
import com.jithin.groceryapp.model.CustomerModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CustomerDataRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CustomerDataRepository {
    companion object {
        const val CUSTOMER_LIST = "customer_list"
    }

    private val customersCollection = firestore.collection(CUSTOMER_LIST)

    override suspend fun addOrUpdateCustomer(
        customer: CustomerModel
    ): Flow<DataState<Unit>> = flow {
        try {
            emit(DataState.Loading)

            customersCollection
                .document(customer.uid)
                .set(customer)
                .await()

            emit(DataState.Success(Unit))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    override suspend fun getCustomerById(
        customerId: String
    ): Flow<DataState<CustomerModel>> = flow {
        try {
            emit(DataState.Loading)

            val snapshot = customersCollection
                .document(customerId)
                .get()
                .await()

            val customer = snapshot.toObject(CustomerModel::class.java)

            if (customer != null) {
                emit(DataState.Success(customer))
            } else {
                emit(DataState.Error(Exception("Customer not found")))
            }

        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}