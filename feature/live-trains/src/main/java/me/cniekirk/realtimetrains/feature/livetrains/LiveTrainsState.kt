package me.cniekirk.realtimetrains.feature.livetrains

import me.cniekirk.realtimetrains.core.network.models.huxley.StationCrs

data class LiveTrainsState(
    val direction: Direction = Direction.DEPARTING,
    val searchStation: StationCrs? = null,
    val filterStation: StationCrs? = null
)

enum class Direction {
    DEPARTING,
    ARRIVING
}

sealed class LiveTrainsEffect {

    data class NavigateToStationSearch(val isFilterStation: Boolean) : LiveTrainsEffect()

    data class NavigateToDepartureBoard(val stationCrs: String, val stationName: String) : LiveTrainsEffect()
}