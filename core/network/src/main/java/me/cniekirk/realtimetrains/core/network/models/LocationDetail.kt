package me.cniekirk.realtimetrains.core.network.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationDetail(
    @Json(name = "crs")
    val crs: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "destination")
    val destination: List<Destination?>?,
    @Json(name = "displayAs")
    val displayAs: String?,
    @Json(name = "gbttBookedDeparture")
    val gbttBookedDeparture: String?,
    @Json(name = "isCall")
    val isCall: Boolean?,
    @Json(name = "isPublicCall")
    val isPublicCall: Boolean?,
    @Json(name = "origin")
    val origin: List<Origin?>?,
    @Json(name = "platform")
    val platform: String?,
    @Json(name = "platformChanged")
    val platformChanged: Boolean?,
    @Json(name = "platformConfirmed")
    val platformConfirmed: Boolean?,
    @Json(name = "realtimeActivated")
    val realtimeActivated: Boolean?,
    @Json(name = "realtimeDeparture")
    val realtimeDeparture: String?,
    @Json(name = "realtimeDepartureActual")
    val realtimeDepartureActual: Boolean?,
    @Json(name = "serviceLocation")
    val serviceLocation: String?,
    @Json(name = "tiploc")
    val tiploc: String?
)