package me.cniekirk.realtimetrains.core.data.model

import androidx.annotation.StringRes
import kotlinx.collections.immutable.ImmutableList
import me.cniekirk.realtimetrains.core.data.R

data class DepartureBoard(
    val locationName: String,
    val services: ImmutableList<DepartureBoardTrainService>
)

data class DepartureBoardTrainService(
    val id: String,
    val origin: String,
    val destination: String,
    val platform: String,
    val platformConfirmed: Boolean,
    val trainLocation: TrainLocation,
    val scheduledDepartureTime: String,
    val realtimeDepartureTime: String,
    val departureTimeActual: Boolean
)

sealed class TrainLocation(@StringRes val status: Int) {

    data object ApproachingStation : TrainLocation(R.string.approaching_station)

    data object Arriving : TrainLocation(R.string.arriving_at_station)

    data object AtPlatform : TrainLocation(R.string.at_platform)

    data object PreparingDeparture : TrainLocation(R.string.preparing_to_depart)

    data object ReadyToDepart : TrainLocation(R.string.ready_to_depart)

    data object Unknown : TrainLocation(R.string.empty_string)
}
