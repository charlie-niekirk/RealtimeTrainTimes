package me.cniekirk.realtimetrains.core.data.repository

import me.cniekirk.realtimetrains.core.common.Result
import me.cniekirk.realtimetrains.core.data.model.DepartureBoard

interface RealtimeTrainsRepository {

    suspend fun getDepartureBoard(station: String): Result<DepartureBoard>
}