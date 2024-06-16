package me.cniekirk.realtimetrains.core.common

data class RecentTrainSearch(
    val direction: Direction,
    val searchName: String,
    val searchCrs: String,
    val filterName: String,
    val filterCrs: String
)