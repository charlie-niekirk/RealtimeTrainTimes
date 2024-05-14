package me.cniekirk.realtimetrains.core.data.repository

import kotlinx.collections.immutable.ImmutableList
import me.cniekirk.realtimetrains.core.common.Result
import me.cniekirk.realtimetrains.core.network.models.huxley.StationCrs

interface CrsRepository {

    suspend fun getAllStations(): Result<ImmutableList<StationCrs>>

    suspend fun queryStations(query: String): Result<ImmutableList<StationCrs>>
}