package me.cniekirk.realtimetrains.feature.servicedetails

import me.cniekirk.realtimetrains.core.common.Error
import me.cniekirk.realtimetrains.core.network.models.ServiceDetails
import me.cniekirk.realtimetrains.core.network.models.huxley.StationCrs

data class ServiceDetailsState(
    val isLoading: Boolean = true,
    val filterStation: StationCrs? = null,
    val serviceDetails: ServiceDetails? = null
)

sealed class ServiceDetailsEffect {

    data object NavigateBack : ServiceDetailsEffect()

    data class DisplayError(val error: Error) : ServiceDetailsEffect()
}