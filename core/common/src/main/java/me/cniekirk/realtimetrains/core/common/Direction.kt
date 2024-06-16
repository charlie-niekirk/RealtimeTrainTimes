package me.cniekirk.realtimetrains.core.common

import kotlinx.serialization.Serializable

@Serializable
enum class Direction {
    DEPARTING,
    ARRIVING
}