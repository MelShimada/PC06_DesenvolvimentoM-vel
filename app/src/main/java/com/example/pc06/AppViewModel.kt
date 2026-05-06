package com.example.pc06

import androidx.camera.core.ImageCapture
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {
    var photoUri by mutableStateOf<String?>(null)
        private set

    lateinit var imageCapture : ImageCapture

    fun setPhoto(uri : String) {
        photoUri = uri
    }
}