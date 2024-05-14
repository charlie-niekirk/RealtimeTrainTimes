package me.cniekirk.realtimetrains.feature.stationsearch

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.cniekirk.realtimetrains.core.network.models.huxley.StationCrs

data class StationSearchState(
    val searchQuery: String = "",
    val searchExpanded: Boolean = false,
    val recentSearches: ImmutableList<String> = persistentListOf(),
    val trainStations: ImmutableList<StationCrs> = persistentListOf()
)

sealed class StationSearchEffect {

}