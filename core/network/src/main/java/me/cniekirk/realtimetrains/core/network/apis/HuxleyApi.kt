package me.cniekirk.realtimetrains.core.network.apis

import me.cniekirk.realtimetrains.core.network.models.huxley.StationCrs
import retrofit2.Response
import retrofit2.http.GET

interface HuxleyApi {

    @GET("crs")
    suspend fun getAllStations(): Response<List<StationCrs>>
}