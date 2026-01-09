package com.jithin.groceryapp.network


/*
 * --------------------------------------------------------------------------
 * File: AuthRepositoryImpl.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

import android.app.Activity
import androidx.credentials.*
import com.google.android.libraries.identity.googleid.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.jithin.groceryapp.R
import com.jithin.groceryapp.domain.DataState
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val credentialManager: CredentialManager
) : AuthRepository {

    override suspend fun signInWithGoogle(activity: Activity) = flow {
        try {
            emit(DataState.Loading)

            // 1️⃣ Google ID option
            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId(
                    activity.getString(R.string.default_web_client_id)
                )
                .setFilterByAuthorizedAccounts(false)
                .build()

            // 2️⃣ Credential request
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            // 3️⃣ Launch Credential Manager
            val result = credentialManager.getCredential(
                context = activity,
                request = request
            )

            val credential = result.credential

            // 4️⃣ Handle Google ID Token
            if (
                credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(credential.data)

                // 5️⃣ Firebase authentication
                val firebaseCredential =
                    GoogleAuthProvider.getCredential(
                        googleIdTokenCredential.idToken,
                        null
                    )

                firebaseAuth.signInWithCredential(firebaseCredential).await()

                emit(DataState.Success(Unit))
            } else {
                emit(DataState.Error(Exception("Invalid credential type")))
            }

        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    override suspend fun signOut() = flow {
        try {
            emit(DataState.Loading)

            firebaseAuth.signOut()

            credentialManager.clearCredentialState(
                ClearCredentialStateRequest()
            )

            emit(DataState.Success(Unit))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}
