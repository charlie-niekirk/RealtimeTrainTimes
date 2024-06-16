package me.cniekirk.realtimetrains.core.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.mapNotNull
import me.cniekirk.realtimetrains.core.common.Direction
import me.cniekirk.realtimetrains.core.common.RecentTrainSearch
import me.cniekirk.realtimetrains.core.datastore.RecentSearches.RecentSearch
import javax.inject.Inject

class RecentSearchesDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<RecentSearches>
) : RecentSearchesDataSource {

    override val recentTrainSearches = dataStore.data.mapNotNull { searches ->
        searches.recentSearchesList.map { search ->
            RecentTrainSearch(
                if (search.isDeparture) Direction.DEPARTING else Direction.ARRIVING,
                search.searchStationName,
                search.searchStationCrs,
                search.filterStationName,
                search.filterStationCrs
            )
        }
    }

    override suspend fun addRecentSearch(recentTrainSearch: RecentTrainSearch) {
        dataStore.updateData { currentData ->
            val recentSearch = RecentSearch.newBuilder()
                .setIsDeparture(recentTrainSearch.direction == Direction.DEPARTING)
                .setSearchStationCrs(recentTrainSearch.searchCrs)
                .setSearchStationName(recentTrainSearch.searchName)
                .setFilterStationCrs(recentTrainSearch.filterCrs)
                .setFilterStationName(recentTrainSearch.filterName)
                .build()

            val searches = currentData.recentSearchesList.toMutableList()

            if (!currentData.recentSearchesList.contains(recentSearch)) {
                searches.add(recentSearch)
            }

            currentData.toBuilder()
                .clearRecentSearches()
                .addAllRecentSearches(searches)
                .build()
        }
    }
}