package me.cniekirk.realtimetrains.core.datastore

import kotlinx.coroutines.flow.Flow
import me.cniekirk.realtimetrains.core.common.RecentTrainSearch

interface RecentSearchesDataSource {

    val recentTrainSearches: Flow<List<RecentTrainSearch>>

    suspend fun addRecentSearch(recentTrainSearch: RecentTrainSearch)
}