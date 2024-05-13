package me.cniekirk.realtimetrains.core.network.apis

import me.cniekirk.realtimetrains.core.network.models.LocationLineUp
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RealtimeTrainsApi {

    @GET("search/{crs}/{year}/{month}/{day}/{time}")
    suspend fun getServices(
        @Path("crs") crs: String,
        @Path("year") year: String,
        @Path("month") month: String,
        @Path("day") day: String,
        @Path("time") time: String
    ): Response<LocationLineUp>
}