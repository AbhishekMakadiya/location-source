package com.location.location.data.repositories

import androidx.lifecycle.LiveData
import com.location.location.database.PlaceDao
import com.location.location.models.PlaceModel
import javax.inject.Inject

class PlaceRepository @Inject constructor(private val placeDao: PlaceDao) {

    fun getAllPlaces(): LiveData<List<PlaceModel>> {
        return placeDao.getAllPlaces()
    }

    suspend fun insertPlace(place: PlaceModel) {
        val existing = placeDao.getAllPlacesList()
        val isPrimary = if (existing.isEmpty()) 1 else 0
        placeDao.insertPlace(place.copy(isPrimary = isPrimary))
    }

    suspend fun deletePlace(place: PlaceModel) {
        placeDao.deletePlace(place)
        if (place.isPrimary == 1) {
            val nextPrimary = placeDao.getOldestPlace()
            nextPrimary?.placeId?.let { placeDao.setPrimary(it) }
        }
    }

    suspend fun updatePlace(place: PlaceModel) {
        placeDao.updatePlace(place)
    }

}