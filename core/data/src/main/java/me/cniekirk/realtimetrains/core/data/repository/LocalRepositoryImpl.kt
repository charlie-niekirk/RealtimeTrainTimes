package me.cniekirk.realtimetrains.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import me.cniekirk.realtimetrains.core.common.Direction
import me.cniekirk.realtimetrains.core.common.RecentTrainSearch
import me.cniekirk.realtimetrains.core.datastore.RecentSearches
import me.cniekirk.realtimetrains.core.datastore.RecentSearchesDataSource
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val recentSearchesDataSource: RecentSearchesDataSource
) : LocalRepository {

    override suspend fun recentSearches(): Flow<List<RecentTrainSearch>> = recentSearchesDataSource.recentTrainSearches
}