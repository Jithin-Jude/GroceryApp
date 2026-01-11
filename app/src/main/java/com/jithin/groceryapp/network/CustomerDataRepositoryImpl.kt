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
import com.google.firebase.firestore.SetOptions
import com.jithin.groceryapp.domain.DataState
import com.jithin.groceryapp.model.CustomerDataModel
import com.jithin.groceryapp.model.toNonNullMap
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

    override suspend fun addCustomer(
        customer: CustomerDataModel
    ): Flow<DataState<Unit>> = flow {
        try {
            emit(DataState.Loading)

            customersCollection
                .document(customer.uid)
                .set(customer.toNonNullMap(), SetOptions.merge())
                .await()

            emit(DataState.Success(Unit))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    override suspend fun updateCustomerFields(
        uid: String,
        fields: Map<String, Any>
    ): Flow<DataState<Unit>> = flow {
        try {
            emit(DataState.Loading)

            customersCollection
                .document(uid)
                .update(fields)
                .await()

            emit(DataState.Success(Unit))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    override suspend fun getCustomerById(
        customerId: String
    ): Flow<DataState<CustomerDataModel>> = flow {
        try {
            emit(DataState.Loading)

            val snapshot = customersCollection
                .document(customerId)
                .get()
                .await()

            val customer = snapshot.toObject(CustomerDataModel::class.java)

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