package me.cniekirk.realtimetrains.core.network.models.huxley


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StationCrs(
    @Json(name = "crsCode")
    val crsCode: String,
    @Json(name = "stationName")
    val stationName: String
)