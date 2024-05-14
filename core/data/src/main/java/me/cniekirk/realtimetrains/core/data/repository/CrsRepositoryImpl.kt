package me.cniekirk.realtimetrains.core.data.repository

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import me.cniekirk.realtimetrains.core.common.Result
import me.cniekirk.realtimetrains.core.database.TrainStation
import me.cniekirk.realtimetrains.core.database.TrainStationDao
import me.cniekirk.realtimetrains.core.network.apis.HuxleyApi
import me.cniekirk.realtimetrains.core.network.models.huxley.StationCrs
import me.cniekirk.realtimetrains.core.network.utils.safeApiCall
import javax.inject.Inject

class CrsRepositoryImpl @Inject constructor(
    private val huxleyApi: HuxleyApi,
    private val trainStationDao: TrainStationDao
) : CrsRepository {

    override suspend fun getAllStations(): Result<ImmutableList<StationCrs>> {
        var stations = trainStationDao.getAll()
        if (stations.isEmpty()) {
            when (val response = safeApiCall { huxleyApi.getAllStations() }) {
                is Result.Failure -> return response
                is Result.Success -> {
                    trainStationDao.insertAll(
                        *response.data.map {
                            TrainStation(stationCrs = it.crsCode, stationName = it.stationName)
                        }.toTypedArray()
                    )
                    stations = trainStationDao.getAll()
                }
            }
        }
        return Result.Success(stations.map { StationCrs(it.stationCrs, it.stationName) }.toImmutableList())
    }

    override suspend fun queryStations(query: String): Result<ImmutableList<StationCrs>> {
        return Result.Success(trainStationDao.findByNameOrCode(query).map { StationCrs(it.stationCrs, it.stationName) }.toImmutableList())
    }
}