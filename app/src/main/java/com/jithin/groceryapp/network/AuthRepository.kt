package com.jithin.groceryapp.network


/*
 * --------------------------------------------------------------------------
 * File: AuthRepository.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */
import android.app.Activity
import com.jithin.groceryapp.domain.DataState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signInWithGoogle(activity: Activity): Flow<DataState<Unit>>
    suspend fun signOut(): Flow<DataState<Unit>>
}