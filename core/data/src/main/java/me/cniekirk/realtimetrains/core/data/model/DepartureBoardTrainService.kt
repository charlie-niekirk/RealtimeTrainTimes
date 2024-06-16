package me.cniekirk.realtimetrains.core.data.model

import androidx.annotation.StringRes
import kotlinx.collections.immutable.ImmutableList
import me.cniekirk.realtimetrains.core.data.R

sealed class StationBoard {

    data class DepartureBoard(
        val locationName: String,
        val services: ImmutableList<DepartureBoardTrainService>
    ) : StationBoard()

    data class ArrivalBoard(
        val locationName: String,
        val services: ImmutableList<ArrivalBoardTrainService>
    ) : StationBoard()
}

data class ArrivalBoardTrainService(
    val id: String,
    val origin: String,
    val destination: String,
    val platform: String,
    val platformConfirmed: Boolean,
    val trainLocation: TrainLocation,
    val scheduledArrivalTime: String,
    val realtimeArrivalTime: String,
    val arrivalTimeActual: Boolean,
    val timeStatus: TimeStatus,
    val trainOperator: String,
    val trainOperatorColor: Long,
    val hasArrived: Boolean,
    val contentType: Int
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
    val departureTimeActual: Boolean,
    val timeStatus: TimeStatus,
    val trainOperator: String,
    val trainOperatorColor: Long,
    val hasDeparted: Boolean,
    val contentType: Int
)

sealed class TrainLocation(@StringRes val status: Int) {

    data object ApproachingStation : TrainLocation(R.string.approaching_station)

    data object Arriving : TrainLocation(R.string.arriving_at_station)

    data object AtPlatform : TrainLocation(R.string.at_platform)

    data object PreparingDeparture : TrainLocation(R.string.preparing_to_depart)

    data object ReadyToDepart : TrainLocation(R.string.ready_to_depart)

    data object Confirmed : TrainLocation(R.string.confirmed)

    data object Estimated : TrainLocation(R.string.estimated)

    data object Arrived : TrainLocation(R.string.arrived)

    data object Departed : TrainLocation(R.string.departed)

    data object NoPlatform : TrainLocation(R.string.no_platform)
}

sealed class TimeStatus {

    data object OnTime : TimeStatus()

    data class Late(val minutes: Int) : TimeStatus()

    data class Cancelled(val reason: String) : TimeStatus()
}
