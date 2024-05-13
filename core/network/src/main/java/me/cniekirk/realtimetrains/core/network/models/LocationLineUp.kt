package me.cniekirk.realtimetrains.core.network.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationLineUp(
    @Json(name = "filter")
    val filter: Any?,
    @Json(name = "location")
    val location: Location?,
    @Json(name = "services")
    val services: List<Service>?
)