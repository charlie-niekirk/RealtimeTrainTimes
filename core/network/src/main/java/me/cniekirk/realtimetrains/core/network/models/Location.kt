package me.cniekirk.realtimetrains.core.network.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "country")
    val country: String?,
    @Json(name = "crs")
    val crs: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "system")
    val system: String?,
    @Json(name = "tiploc")
    val tiploc: List<String?>?
)