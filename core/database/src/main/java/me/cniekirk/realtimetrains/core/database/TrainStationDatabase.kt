package me.cniekirk.realtimetrains.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TrainStation::class], version = 1)
abstract class TrainStationDatabase : RoomDatabase() {
    abstract fun trainStationDao(): TrainStationDao
}