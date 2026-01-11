package com.jithin.groceryapp.domain.repository


/*
 * --------------------------------------------------------------------------
 * File: StorageRepository.dart
 * Developer: <Jithin/Jude>
 * Created: 10/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

import android.net.Uri
import com.jithin.groceryapp.core.common.UploadState
import kotlinx.coroutines.flow.Flow

interface StorageRepository {
    fun uploadProfilePicture(
        uid: String,
        imageUri: Uri
    ): Flow<UploadState>
}