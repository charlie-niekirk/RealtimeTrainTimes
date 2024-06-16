package me.cniekirk.realtimetrains.core.data.repository

import kotlinx.coroutines.flow.Flow
import me.cniekirk.realtimetrains.core.common.Direction
import me.cniekirk.realtimetrains.core.common.RecentTrainSearch
import me.cniekirk.realtimetrains.core.datastore.RecentSearches

interface LocalRepository {

    suspend fun recentSearches(): Flow<List<RecentTrainSearch>>
}