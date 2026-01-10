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
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.jithin.groceryapp.R
import com.jithin.groceryapp.domain.DataState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val credentialManager: CredentialManager
) : AuthRepository {

    /* ---------------- GOOGLE SIGN IN ---------------- */

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



    /* ---------------- PHONE AUTH : REQUEST OTP ---------------- */

    override suspend fun requestOTP(
        activity: Activity,
        phoneNumber: String
    ): Flow<DataState<String>> = callbackFlow {

        trySend(DataState.Loading)

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // Auto-retrieval or instant verification
            }

            override fun onVerificationFailed(e: FirebaseException) {
                trySend(DataState.Error(e))
                close()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                trySend(DataState.Success(verificationId))
            }
        }

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)

        awaitClose { }
    }

    /* ---------------- PHONE AUTH : VERIFY OTP ---------------- */

    override suspend fun verifyOTP(
        verificationId: String,
        otp: String
    ): Flow<DataState<Unit>> = flow {
        try {
            emit(DataState.Loading)

            val credential = PhoneAuthProvider.getCredential(
                verificationId,
                otp
            )

            firebaseAuth.signInWithCredential(credential).await()

            emit(DataState.Success(Unit))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    /* ---------------- SIGN OUT ---------------- */

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

    override fun getLoggedInUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}
