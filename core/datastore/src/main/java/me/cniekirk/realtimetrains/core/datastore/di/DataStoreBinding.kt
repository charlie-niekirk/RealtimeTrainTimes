package me.cniekirk.realtimetrains.core.datastore.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.cniekirk.realtimetrains.core.datastore.RecentSearchesDataSource
import me.cniekirk.realtimetrains.core.datastore.RecentSearchesDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
internal interface DataStoreBinding {

    @Binds
    fun bindsRecentSearchesDataSource(recentSearchesDataSourceImpl: RecentSearchesDataSourceImpl): RecentSearchesDataSource
}