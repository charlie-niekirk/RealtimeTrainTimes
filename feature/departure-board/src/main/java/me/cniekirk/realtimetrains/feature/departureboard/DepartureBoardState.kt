package me.cniekirk.realtimetrains.feature.departureboard

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.cniekirk.realtimetrains.core.common.Direction
import me.cniekirk.realtimetrains.core.data.model.StationBoard

data class DepartureBoardState(
    val stationName: String,
    val isLoading: Boolean = true,
    val direction: Direction,
    val stationBoard: StationBoard? = null
)

sealed class DepartureBoardEffect {

    data class NavigateToServiceDetails(val serviceId: String) : DepartureBoardEffect()
}