package com.jithin.groceryapp.data.repository


/*
 * --------------------------------------------------------------------------
 * File: StorageRepositoryImpl.dart
 * Developer: <Jithin/Jude>
 * Created: 10/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.jithin.groceryapp.core.common.UploadState
import com.jithin.groceryapp.domain.repository.StorageRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage
) : StorageRepository {

    override fun uploadProfilePicture(
        uid: String,
        imageUri: Uri
    ): Flow<UploadState> = callbackFlow {

        trySend(UploadState.Uploading)

        val ref = storage
            .reference
            .child("profile_picture/$uid")

        val uploadTask = ref.putFile(imageUri)

        uploadTask.addOnProgressListener { task ->
            val percent =
                ((task.bytesTransferred * 100) / task.totalByteCount).toInt()
            trySend(UploadState.Progress(percent))
        }

        uploadTask.addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener { uri ->
                trySend(UploadState.Success(uri.toString()))
                close()
            }
        }

        uploadTask.addOnFailureListener { e ->
            trySend(UploadState.Error(e.message ?: "Upload failed"))
            close()
        }

        awaitClose { }
    }
}
