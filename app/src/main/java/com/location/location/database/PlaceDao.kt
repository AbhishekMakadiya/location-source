package com.location.location.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.location.location.models.PlaceModel

@Dao
interface PlaceDao {

    @Insert
    fun insertPlace(mPlaceModel: PlaceModel): Long

    @Query("SELECT * FROM PlaceTable")
    fun getAllPlaces(): LiveData<List<PlaceModel>>

    @Delete
    suspend fun deletePlace(place: PlaceModel)

    @Update
    suspend fun updatePlace(place: PlaceModel)

    @Query("UPDATE PlaceTable SET isPrimary = 0")
    suspend fun clearPrimary()

    @Query("UPDATE PlaceTable SET isPrimary = 1 WHERE placeId = :placeId")
    suspend fun setPrimary(placeId: Int)

    @Query("SELECT * FROM PlaceTable ORDER BY placeId ASC LIMIT 1")
    suspend fun getOldestPlace(): PlaceModel?

    @Query("SELECT * FROM PlaceTable")
    suspend fun getAllPlacesList(): List<PlaceModel>


}