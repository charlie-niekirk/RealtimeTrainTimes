package me.cniekirk.realtimetrains.core.data.mapper

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import me.cniekirk.realtimetrains.core.data.model.ArrivalBoardTrainService
import me.cniekirk.realtimetrains.core.data.model.DepartureBoardTrainService
import me.cniekirk.realtimetrains.core.data.model.StationBoard
import me.cniekirk.realtimetrains.core.data.model.TimeStatus
import me.cniekirk.realtimetrains.core.data.model.TrainLocation
import me.cniekirk.realtimetrains.core.network.models.LocationLineUp
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("HHmm")

internal fun LocationLineUp.toDepartureBoard(): StationBoard.DepartureBoard {
    val servicesMapped = services
        ?.filter { it.serviceType?.equals("train", true) == true }
        ?.map { service ->

        val operatorName = if (service.atocCode?.equals("LD", true) == true) {
            "Lumo"
        } else service.atocName

        val scheduled = LocalTime.parse(service.locationDetail?.gbttBookedDeparture ?: "", formatter)

        if (service.locationDetail?.realtimeDeparture != null) {
            val realtime = LocalTime.parse(service.locationDetail?.realtimeDeparture ?: "", formatter)

            val timeBetween = Duration.between(scheduled, realtime)

            val timeStatus = if (!service.locationDetail?.cancelReasonCode.isNullOrEmpty() && !service.locationDetail?.cancelReasonShortText.isNullOrEmpty()) {
                TimeStatus.Cancelled(service.locationDetail?.cancelReasonShortText!!)
            } else if (timeBetween.isZero or timeBetween.isNegative) {
                TimeStatus.OnTime
            } else {
                TimeStatus.Late(timeBetween.toMinutes().toInt())
            }

            val now = LocalTime.now()

            val departed = now.isAfter(realtime) && service.locationDetail?.realtimeDepartureActual == true

            DepartureBoardTrainService(
                id = service.serviceUid ?: "",
                origin = service.locationDetail?.origin?.first()?.description ?: "",
                destination = service.locationDetail?.destination?.first()?.description ?: "",
                platform = service.locationDetail?.platform ?: "Unknown",
                platformConfirmed = service.locationDetail?.platformConfirmed ?: false,
                trainLocation = getTrainLocation(service.locationDetail?.serviceLocation, inThePast = departed, isDeparting = true, platformEmpty = service.locationDetail?.platform.isNullOrEmpty(), platformConfirmed = service.locationDetail?.platformConfirmed ?: false),
                scheduledDepartureTime = (service.locationDetail?.gbttBookedDeparture ?: "").chunked(2).joinToString(":"),
                realtimeDepartureTime = (service.locationDetail?.realtimeDeparture ?: "").chunked(2).joinToString(":"),
                departureTimeActual = service.locationDetail?.realtimeDepartureActual ?: false,
                timeStatus = timeStatus,
                trainOperator = operatorName ?: "",
                trainOperatorColor = codeToOperatorColor(service.atocCode),
                hasDeparted = departed,
                contentType = if (timeStatus is TimeStatus.OnTime) 1 else 2
            )
        } else {
            DepartureBoardTrainService(
                id = service.serviceUid ?: "",
                origin = service.locationDetail?.origin?.first()?.description ?: "",
                destination = service.locationDetail?.destination?.first()?.description ?: "",
                platform = service.locationDetail?.platform ?: "Unknown",
                platformConfirmed = service.locationDetail?.platformConfirmed ?: false,
                trainLocation = getTrainLocation(service.locationDetail?.serviceLocation, inThePast = false, isDeparting = true, platformEmpty = service.locationDetail?.platform.isNullOrEmpty(), platformConfirmed = service.locationDetail?.platformConfirmed ?: false),
                scheduledDepartureTime = (service.locationDetail?.gbttBookedDeparture ?: "").chunked(2).joinToString(":"),
                realtimeDepartureTime = (service.locationDetail?.realtimeDeparture ?: "").chunked(2).joinToString(":"),
                departureTimeActual = service.locationDetail?.realtimeDepartureActual ?: false,
                timeStatus = TimeStatus.OnTime,
                trainOperator = operatorName ?: "",
                trainOperatorColor = codeToOperatorColor(service.atocCode),
                hasDeparted = false,
                contentType = 1
            )
        }
    }

    val services = servicesMapped?.toImmutableList() ?: persistentListOf()

    return StationBoard.DepartureBoard(
        locationName = location?.name ?: "",
        services = services
    )
}

internal fun LocationLineUp.toArrivalBoard(): StationBoard.ArrivalBoard {
    val servicesMapped = services
        ?.filter { it.serviceType?.equals("train", true) == true }
        ?.map { service ->

        val operatorName = if (service.atocCode?.equals("LD", true) == true) {
            "Lumo"
        } else service.atocName

        if (service.locationDetail?.realtimeArrival != null) {
            val scheduled = LocalTime.parse(service.locationDetail?.gbttBookedArrival ?: "", formatter)
            val realtime = LocalTime.parse(service.locationDetail?.realtimeArrival, formatter)

            val timeBetween = Duration.between(scheduled, realtime)

            val timeStatus = if (!service.locationDetail?.cancelReasonCode.isNullOrEmpty() && !service.locationDetail?.cancelReasonShortText.isNullOrEmpty()) {
                TimeStatus.Cancelled(service.locationDetail?.cancelReasonShortText!!)
            } else if (timeBetween.isZero or timeBetween.isNegative) {
                TimeStatus.OnTime
            } else {
                TimeStatus.Late(timeBetween.toMinutes().toInt())
            }

            val now = LocalTime.now()

            val arrived = now.isAfter(realtime) && service.locationDetail?.realtimeArrivalActual == true

            ArrivalBoardTrainService(
                id = service.serviceUid ?: "",
                origin = service.locationDetail?.origin?.first()?.description ?: "",
                destination = service.locationDetail?.destination?.first()?.description ?: "",
                platform = service.locationDetail?.platform ?: "Unknown",
                platformConfirmed = service.locationDetail?.platformConfirmed ?: false,
                trainLocation = getTrainLocation(service.locationDetail?.serviceLocation, inThePast = arrived, isDeparting = false, platformEmpty = service.locationDetail?.platform.isNullOrEmpty(), platformConfirmed = service.locationDetail?.platformConfirmed ?: false),
                scheduledArrivalTime = (service.locationDetail?.gbttBookedArrival ?: "").chunked(2).joinToString(":"),
                realtimeArrivalTime = (service.locationDetail?.realtimeArrival ?: "").chunked(2).joinToString(":"),
                arrivalTimeActual = service.locationDetail?.realtimeArrivalActual ?: false,
                timeStatus = timeStatus,
                trainOperator = operatorName ?: "",
                trainOperatorColor = codeToOperatorColor(service.atocCode),
                hasArrived = arrived,
                contentType = if (timeStatus is TimeStatus.OnTime) 1 else 2
            )
        } else {
            ArrivalBoardTrainService(
                id = service.serviceUid ?: "",
                origin = service.locationDetail?.origin?.first()?.description ?: "",
                destination = service.locationDetail?.destination?.first()?.description ?: "",
                platform = service.locationDetail?.platform ?: "Unknown",
                platformConfirmed = service.locationDetail?.platformConfirmed ?: false,
                trainLocation = getTrainLocation(service.locationDetail?.serviceLocation, inThePast = false, isDeparting = false, platformEmpty = service.locationDetail?.platform.isNullOrEmpty(), platformConfirmed = service.locationDetail?.platformConfirmed ?: false),
                scheduledArrivalTime = (service.locationDetail?.gbttBookedArrival ?: "").chunked(2).joinToString(":"),
                realtimeArrivalTime = (service.locationDetail?.realtimeArrival ?: "").chunked(2).joinToString(":"),
                arrivalTimeActual = service.locationDetail?.realtimeArrivalActual ?: false,
                timeStatus = TimeStatus.OnTime,
                trainOperator = operatorName ?: "",
                trainOperatorColor = codeToOperatorColor(service.atocCode),
                hasArrived = false,
                contentType = 1
            )
        }
    }

    val services = servicesMapped?.toImmutableList() ?: persistentListOf()

    return StationBoard.ArrivalBoard(
        locationName = location?.name ?: "",
        services = services
    )
}

private fun getTrainLocation(
    locationString: String?,
    inThePast: Boolean,
    isDeparting: Boolean,
    platformEmpty: Boolean,
    platformConfirmed: Boolean
): TrainLocation {
    return when (locationString) {
        "APPR_STAT" -> TrainLocation.ApproachingStation
        "APPR_PLAT" -> TrainLocation.Arriving
        "AT_PLAT" -> TrainLocation.AtPlatform
        "DEP_PREP" -> TrainLocation.PreparingDeparture
        "DEP_READY" -> TrainLocation.ReadyToDepart
        else -> if (isDeparting) {
            if (inThePast) {
                TrainLocation.Departed
            } else {
                if (platformEmpty) {
                    TrainLocation.NoPlatform
                } else {
                    if (platformConfirmed) {
                        TrainLocation.Confirmed
                    } else {
                        TrainLocation.Estimated
                    }
                }
            }
        } else {
            if (inThePast) {
                TrainLocation.Arrived
            } else {
                if (platformEmpty) {
                    TrainLocation.NoPlatform
                } else {
                    if (platformConfirmed) {
                        TrainLocation.Confirmed
                    } else {
                        TrainLocation.Estimated
                    }
                }
            }
        }
    }
}

fun codeToOperatorColor(code: String?): Long {
    return when (code) {
        "GW" -> 0xff0a493e
        "XR" -> 0xff6950a1
        "HX" -> 0xff532e63
        "SW" -> 0xff24398c
        "XC" -> 0xff660f21
        "AW" -> 0xffff0000
        "VT" -> 0xff004354
        "LM" -> 0xffff8300
        "GR" -> 0xffce0e2d
        "TP" -> 0xff09a4ec
        "NT" -> 0xff0f0d78
        "SE" -> 0xff389cff
        "TL" -> 0xffff5aa4
        "SR" -> 0xff1e467d
        "LE" -> 0xffd70428
        "EM" -> 0xff713563
        "HT" -> 0xffde005c
        "IL" -> 0xff1e90ff
        "SN" -> 0xff8cc63e
        "LD" -> 0xff2b6ef5
        "ES" -> 0xff086bfe
        "CC" -> 0xffb7007c
        "LO" -> 0xffee7c0e
        else -> 0xff000000
    }
}