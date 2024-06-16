package me.cniekirk.realtimetrains.core.network.apis

import me.cniekirk.realtimetrains.core.network.models.LocationLineUp
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RealtimeTrainsApi {

    @GET("search/{crs}/{year}/{month}/{day}/{time}")
    suspend fun getDepartures(
        @Path("crs") crs: String,
        @Path("year") year: String,
        @Path("month") month: String,
        @Path("day") day: String,
        @Path("time") time: String
    ): Response<LocationLineUp>

    @GET("search/{crs}/to/{filterCrs}/{year}/{month}/{day}/{time}")
    suspend fun getDeparturesFiltered(
        @Path("crs") crs: String,
        @Path("filterCrs") filterCrs: String,
        @Path("year") year: String,
        @Path("month") month: String,
        @Path("day") day: String,
        @Path("time") time: String
    ): Response<LocationLineUp>

    @GET("search/{crs}/{year}/{month}/{day}/{time}/arrivals")
    suspend fun getArrivals(
        @Path("crs") crs: String,
        @Path("year") year: String,
        @Path("month") month: String,
        @Path("day") day: String,
        @Path("time") time: String
    ): Response<LocationLineUp>

    @GET("search/{crs}/from/{filterCrs}/{year}/{month}/{day}/{time}/arrivals")
    suspend fun getArrivalsFiltered(
        @Path("crs") crs: String,
        @Path("filterCrs") filterCrs: String,
        @Path("year") year: String,
        @Path("month") month: String,
        @Path("day") day: String,
        @Path("time") time: String
    ): Response<LocationLineUp>
}