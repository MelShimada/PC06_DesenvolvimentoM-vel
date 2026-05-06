package com.example.pc06.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoPinDao {
    @Insert
    suspend fun insert(pin: PhotoPin)

    @Query("SELECT * FROM PhotoPin ORDER BY timestamp DESC")
    fun getAll(): Flow<List<PhotoPin>>
}
