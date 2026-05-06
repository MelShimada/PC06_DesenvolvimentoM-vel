package com.example.pc06.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhotoPin(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val photoPath: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long
)
