package me.cniekirk.realtimetrains.core.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ServiceDetails(
    @Json(name = "serviceUid")
    val serviceUid: String,
    @Json(name = "runDate")
    val runDate: String,
    @Json(name = "serviceType")
    val serviceType: String,
    @Json(name = "isPassenger")
    val isPassenger: Boolean,
    @Json(name = "trainIdentity")
    val trainIdentity: String,
    @Json(name = "powerType")
    val powerType: String,
    @Json(name = "trainClass")
    val trainClass: String?,
    @Json(name = "sleeper")
    val sleeper: String?,
    @Json(name = "atocCode")
    val atocCode: String,
    @Json(name = "atocName")
    val atocName: String,
    @Json(name = "performanceMonitored")
    val performanceMonitored: Boolean,
    @Json(name = "realtimeActivated")
    val realtimeActivated: Boolean,
    @Json(name = "runningIdentity")
    val runningIdentity: String?,
    @Json(name = "origin")
    val origin: List<Origin>,
    @Json(name = "destination")
    val destination: List<Destination>,
    @Json(name = "locations")
    val locations: List<LocationDetail>
)
