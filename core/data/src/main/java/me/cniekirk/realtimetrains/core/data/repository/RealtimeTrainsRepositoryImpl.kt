package me.cniekirk.realtimetrains.core.data.repository

import me.cniekirk.realtimetrains.core.common.Direction
import me.cniekirk.realtimetrains.core.common.Direction.ARRIVING
import me.cniekirk.realtimetrains.core.common.Direction.DEPARTING
import me.cniekirk.realtimetrains.core.common.RecentTrainSearch
import me.cniekirk.realtimetrains.core.common.Result
import me.cniekirk.realtimetrains.core.data.mapper.toArrivalBoard
import me.cniekirk.realtimetrains.core.data.mapper.toDepartureBoard
import me.cniekirk.realtimetrains.core.data.model.StationBoard
import me.cniekirk.realtimetrains.core.datastore.RecentSearchesDataSource
import me.cniekirk.realtimetrains.core.network.apis.RealtimeTrainsApi
import me.cniekirk.realtimetrains.core.network.models.huxley.StationCrs
import me.cniekirk.realtimetrains.core.network.utils.safeApiCall
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class RealtimeTrainsRepositoryImpl @Inject constructor(
    private val realtimeTrainsApi: RealtimeTrainsApi,
    private val recentSearchesDataSource: RecentSearchesDataSource,
) : RealtimeTrainsRepository {

    private val timeFormat = DateTimeFormatter.ofPattern("HHmm")

    override suspend fun getStationBoard(searchStation: StationCrs, filterStation: StationCrs?, direction: Direction): Result<StationBoard> {
        val dateTime = LocalDateTime.now()

        return when (direction) {
            DEPARTING -> getDepartures(searchStation, filterStation, dateTime)
            ARRIVING -> getArrivals(searchStation, filterStation, dateTime)
        }
    }

    private suspend fun getDepartures(
        searchStation: StationCrs,
        filterStation: StationCrs?,
        dateTime: LocalDateTime
    ): Result<StationBoard.DepartureBoard> {
        recentSearchesDataSource.addRecentSearch(
            RecentTrainSearch(
                direction = DEPARTING,
                searchName = searchStation.stationName,
                searchCrs = searchStation.crsCode,
                filterName = filterStation?.stationName ?: "",
                filterCrs = filterStation?.crsCode ?: ""
            )
        )

        val month = if (dateTime.monthValue < 10) {
            "0${dateTime.monthValue}"
        } else {
            dateTime.monthValue.toString()
        }

        if (filterStation != null) {

            return when (val response = safeApiCall {
                realtimeTrainsApi.getDeparturesFiltered(
                    searchStation.crsCode,
                    filterStation.crsCode,
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
        } else {
            return when (val response = safeApiCall {
                realtimeTrainsApi.getDepartures(
                    searchStation.crsCode,
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

    private suspend fun getArrivals(
        searchStation: StationCrs,
        filterStation: StationCrs?,
        dateTime: LocalDateTime
    ): Result<StationBoard.ArrivalBoard> {
        recentSearchesDataSource.addRecentSearch(
            RecentTrainSearch(
                direction = ARRIVING,
                searchName = searchStation.stationName,
                searchCrs = searchStation.crsCode,
                filterName = filterStation?.stationName ?: "",
                filterCrs = filterStation?.crsCode ?: ""
            )
        )

        val month = if (dateTime.monthValue < 10) {
            "0${dateTime.monthValue}"
        } else {
            dateTime.monthValue.toString()
        }

        if (filterStation != null) {
            return when (val response = safeApiCall {
                realtimeTrainsApi.getArrivalsFiltered(
                    searchStation.crsCode,
                    filterStation.crsCode,
                    dateTime.year.toString(),
                    month,
                    dateTime.dayOfMonth.toString(),
                    timeFormat.format(dateTime)
                )
            }
            ) {
                is Result.Failure -> response
                is Result.Success -> Result.Success(response.data.toArrivalBoard())
            }
        } else {
            return when (val response = safeApiCall {
                realtimeTrainsApi.getArrivals(
                    searchStation.crsCode,
                    dateTime.year.toString(),
                    month,
                    dateTime.dayOfMonth.toString(),
                    timeFormat.format(dateTime)
                )
            }
            ) {
                is Result.Failure -> response
                is Result.Success -> Result.Success(response.data.toArrivalBoard())
            }
        }
    }
}