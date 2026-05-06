package com.example.pc06.data

class PhotoPinRepository(private val dao: PhotoPinDao) {
    val allPins = dao.getAll()

    suspend fun insert(pin: PhotoPin) = dao.insert(pin)
}
