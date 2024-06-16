package me.cniekirk.realtimetrains.feature.livetrains

import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import me.cniekirk.realtimetrains.core.common.Direction
import me.cniekirk.realtimetrains.core.common.RecentTrainSearch
import me.cniekirk.realtimetrains.core.network.models.huxley.StationCrs

data class LiveTrainsState(
    val direction: Direction = Direction.DEPARTING,
    val searchStation: StationCrs? = null,
    val filterStation: StationCrs? = null,
    val recentTrainSearches: Flow<ImmutableList<RecentTrainSearch>> = flowOf()
)

sealed class LiveTrainsEffect {

    data class NavigateToStationSearch(val isFilterStation: Boolean) : LiveTrainsEffect()

    data class NavigateToDepartureBoard(
        val searchStation: StationCrs,
        val filterStation: StationCrs?,
        val direction: Direction
    ) : LiveTrainsEffect()
}