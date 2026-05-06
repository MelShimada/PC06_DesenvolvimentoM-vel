package com.example.pc06

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pc06.data.AppDatabase
import com.example.pc06.data.PhotoPin
import com.example.pc06.data.PhotoPinRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = PhotoPinRepository(AppDatabase.getInstance(application).photoPinDao())

    val pins: StateFlow<List<PhotoPin>> = repo.allPins.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList()
    )

    fun savePin(photoPath: String, lat: Double, lng: Double) {
        viewModelScope.launch {
            repo.insert(PhotoPin(photoPath = photoPath, latitude = lat, longitude = lng, timestamp = System.currentTimeMillis()))
        }
    }
}
