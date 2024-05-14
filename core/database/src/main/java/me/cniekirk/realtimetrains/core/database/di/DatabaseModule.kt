package me.cniekirk.realtimetrains.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.cniekirk.realtimetrains.core.database.TrainStationDao
import me.cniekirk.realtimetrains.core.database.TrainStationDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun providesTrainStationDatabase(@ApplicationContext context: Context): TrainStationDatabase {
        return Room.databaseBuilder(
            context,
            TrainStationDatabase::class.java,
            "train_station_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providesTrainStationDao(trainStationDatabase: TrainStationDatabase): TrainStationDao =
        trainStationDatabase.trainStationDao()
}