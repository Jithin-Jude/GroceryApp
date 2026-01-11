package com.jithin.groceryapp.core.common


/*
 * --------------------------------------------------------------------------
 * File: UploadState.dart
 * Developer: <Jithin/Jude>
 * Created: 10/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

sealed class UploadState {
    object Idle : UploadState()
    object Uploading : UploadState()
    data class Progress(val percent: Int) : UploadState()
    data class Success(val downloadUrl: String) : UploadState()
    data class Error(val message: String) : UploadState()
}