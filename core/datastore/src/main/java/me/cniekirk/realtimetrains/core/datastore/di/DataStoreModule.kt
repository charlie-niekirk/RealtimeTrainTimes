package me.cniekirk.realtimetrains.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.cniekirk.realtimetrains.core.datastore.RecentSearches
import me.cniekirk.realtimetrains.core.datastore.RecentSearchesSerializer
import javax.inject.Singleton

private const val DATA_STORE_FILE_NAME = "recent_searches.proto"

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    @Provides
    @Singleton
    fun providesRecentSearchesDatastore(@ApplicationContext context: Context): DataStore<RecentSearches> {
        return DataStoreFactory.create(
            serializer = RecentSearchesSerializer,
            produceFile = { context.dataStoreFile(DATA_STORE_FILE_NAME) },
            corruptionHandler = null,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }
}