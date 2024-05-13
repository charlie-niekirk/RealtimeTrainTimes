package me.cniekirk.realtimetrains.core.data.repository

import me.cniekirk.realtimetrains.core.common.Result
import me.cniekirk.realtimetrains.core.data.mapper.toDepartureBoard
import me.cniekirk.realtimetrains.core.data.model.DepartureBoard
import me.cniekirk.realtimetrains.core.network.apis.RealtimeTrainsApi
import me.cniekirk.realtimetrains.core.network.models.LocationLineUp
import me.cniekirk.realtimetrains.core.network.utils.safeApiCall
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class RealtimeTrainsRepositoryImpl @Inject constructor(
    private val realtimeTrainsApi: RealtimeTrainsApi
) : RealtimeTrainsRepository {

    override suspend fun getDepartureBoard(station: String): Result<DepartureBoard> {
        val dateTime = LocalDateTime.now()
        val timeFormat = DateTimeFormatter.ofPattern("HHmm")
        val month = if (dateTime.monthValue < 10) {
            "0${dateTime.monthValue}"
        } else {
            dateTime.monthValue.toString()
        }

        return when (val response = safeApiCall {
                realtimeTrainsApi.getServices(
                    station,
                    dateTime.year.toString(),
                    month,
                    dateTime.dayOfMonth.toString(),
                    timeFormat.format(dateTime)
                )
            }
        ) {
            is Result.Failure -> response
            is Result.Success -> Result.Success(response.data.toDepartureBoard())
        }
    }
}