package me.cniekirk.realtimetrains.feature.stationsearch

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.cniekirk.realtimetrains.core.network.models.huxley.StationCrs

data class StationSearchState(
    val searchQuery: String = "",
    val recentSearches: ImmutableList<String> = persistentListOf(),
    val trainStations: ImmutableList<StationCrs> = persistentListOf(),
)

sealed class StationSearchEffect {

    data class StationSelected(
        val stationCrs: String,
        val stationName: String,
        val stationType: String
    ) : StationSearchEffect()
}