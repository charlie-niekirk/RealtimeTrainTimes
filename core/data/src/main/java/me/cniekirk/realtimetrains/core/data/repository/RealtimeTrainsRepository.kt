package me.cniekirk.realtimetrains.core.data.repository

import me.cniekirk.realtimetrains.core.common.Direction
import me.cniekirk.realtimetrains.core.common.Result
import me.cniekirk.realtimetrains.core.data.model.StationBoard
import me.cniekirk.realtimetrains.core.network.models.ServiceDetails
import me.cniekirk.realtimetrains.core.network.models.huxley.StationCrs

interface RealtimeTrainsRepository {

    suspend fun getStationBoard(
        searchStation: StationCrs,
        filterStation: StationCrs? = null,
        direction: Direction
    ): Result<StationBoard>

    suspend fun getServiceDetails(
        serviceUid: String
    ): Result<ServiceDetails>
}