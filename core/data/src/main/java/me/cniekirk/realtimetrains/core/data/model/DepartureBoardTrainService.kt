package me.cniekirk.realtimetrains.core.data.model

import kotlinx.collections.immutable.ImmutableList

data class DepartureBoard(
    val locationName: String,
    val services: ImmutableList<DepartureBoardTrainService>
)

data class DepartureBoardTrainService(
    val origin: String,
    val destination: String,
    val platform: String,
    val platformConfirmed: Boolean,
    val trainLocation: TrainLocation,
    val scheduledDepartureTime: String,
    val realtimeDepartureTime: String,
    val departureTimeActual: Boolean
)

sealed class TrainLocation {

    data object ApproachingStation : TrainLocation()

    data object Arriving : TrainLocation()

    data object AtPlatform : TrainLocation()

    data object PreparingDeparture : TrainLocation()

    data object ReadyToDepart : TrainLocation()

    data object Unknown : TrainLocation()
}
