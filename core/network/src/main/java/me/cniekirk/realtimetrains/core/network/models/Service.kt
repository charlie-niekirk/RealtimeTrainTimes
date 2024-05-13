package me.cniekirk.realtimetrains.core.network.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Service(
    @Json(name = "atocCode")
    val atocCode: String?,
    @Json(name = "atocName")
    val atocName: String?,
    @Json(name = "isPassenger")
    val isPassenger: Boolean?,
    @Json(name = "locationDetail")
    val locationDetail: LocationDetail?,
    @Json(name = "runDate")
    val runDate: String?,
    @Json(name = "runningIdentity")
    val runningIdentity: String?,
    @Json(name = "serviceType")
    val serviceType: String?,
    @Json(name = "serviceUid")
    val serviceUid: String?,
    @Json(name = "trainIdentity")
    val trainIdentity: String?
)