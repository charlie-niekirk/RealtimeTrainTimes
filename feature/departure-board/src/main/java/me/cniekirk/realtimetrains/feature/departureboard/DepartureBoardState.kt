package me.cniekirk.realtimetrains.feature.departureboard

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.cniekirk.realtimetrains.core.data.model.DepartureBoardTrainService

data class DepartureBoardState(
    val stationName: String,
    val isLoading: Boolean = true,
    val services: ImmutableList<DepartureBoardTrainService> = persistentListOf()
)

sealed class DepartureBoardEffect {

    data class NavigateToServiceDetails(val serviceId: String) : DepartureBoardEffect()
}