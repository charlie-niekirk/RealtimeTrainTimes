package me.cniekirk.realtimetrains.feature.stationsearch

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object StationSearch

fun NavGraphBuilder.stationSearch(navigateToDepartureBoard: (String, String) -> Unit) {
    composable<StationSearch> {
        val viewModel = hiltViewModel<StationSearchViewModel>()
        StationSearchScreen(viewModel = viewModel) { stationCrs, stationName ->
            navigateToDepartureBoard(stationCrs, stationName)
        }
    }
}