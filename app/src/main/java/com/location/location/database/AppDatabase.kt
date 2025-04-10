package com.location.location.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.location.location.models.PlaceModel


@Database(entities = [PlaceModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
}