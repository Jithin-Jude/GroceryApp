package com.jithin.groceryapp.domain.repository


/*
 * --------------------------------------------------------------------------
 * File: AuthRepository.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */
import android.app.Activity
import com.google.firebase.auth.FirebaseUser
import com.jithin.groceryapp.core.common.DataState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signInWithGoogle(activity: Activity): Flow<DataState<Unit>>

    suspend fun requestOTP(
        activity: Activity,
        phoneNumber: String
    ): Flow<DataState<String>> // emits verificationId

    suspend fun verifyOTP(
        verificationId: String,
        otp: String
    ): Flow<DataState<Unit>>

    suspend fun signOut(): Flow<DataState<Unit>>
    fun getLoggedInUser(): FirebaseUser?
}