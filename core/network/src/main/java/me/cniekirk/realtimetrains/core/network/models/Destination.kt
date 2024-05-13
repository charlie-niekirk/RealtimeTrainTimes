package me.cniekirk.realtimetrains.core.network.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Destination(
    @Json(name = "description")
    val description: String?,
    @Json(name = "publicTime")
    val publicTime: String?,
    @Json(name = "tiploc")
    val tiploc: String?,
    @Json(name = "workingTime")
    val workingTime: String?
)