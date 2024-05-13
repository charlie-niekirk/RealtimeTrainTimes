package me.cniekirk.realtimetrains.core.data.mapper

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import me.cniekirk.realtimetrains.core.data.model.DepartureBoard
import me.cniekirk.realtimetrains.core.data.model.DepartureBoardTrainService
import me.cniekirk.realtimetrains.core.data.model.TrainLocation
import me.cniekirk.realtimetrains.core.network.models.LocationLineUp

internal fun LocationLineUp.toDepartureBoard(): DepartureBoard {
    val servicesMapped = services?.map { service ->
        DepartureBoardTrainService(
            origin = service.locationDetail?.origin?.first()?.description ?: "",
            destination = service.locationDetail?.destination?.first()?.description ?: "",
            platform = service.locationDetail?.platform ?: "",
            platformConfirmed = service.locationDetail?.platformConfirmed ?: false,
            trainLocation = when (service.locationDetail?.serviceLocation) {
                "APPR_STAT" -> TrainLocation.ApproachingStation
                "APPR_PLAT" -> TrainLocation.Arriving
                "AT_PLAT" -> TrainLocation.AtPlatform
                "DEP_PREP" -> TrainLocation.PreparingDeparture
                "DEP_READY" -> TrainLocation.ReadyToDepart
                else -> TrainLocation.Unknown
            },
            scheduledDepartureTime = (service.locationDetail?.gbttBookedDeparture ?: "").chunked(2).joinToString(":"),
            realtimeDepartureTime = (service.locationDetail?.realtimeDeparture ?: "").chunked(2).joinToString(":"),
            departureTimeActual = service.locationDetail?.realtimeDepartureActual ?: false
        )
    }

    val services = servicesMapped?.toImmutableList() ?: persistentListOf()

    return DepartureBoard(
        locationName = location?.name ?: "",
        services = services
    )
}