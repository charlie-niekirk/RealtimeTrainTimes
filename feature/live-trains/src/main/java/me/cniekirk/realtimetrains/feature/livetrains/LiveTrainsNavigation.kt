package me.cniekirk.realtimetrains.feature.livetrains

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object LiveTrains

fun NavGraphBuilder.liveTrains(navigateToDepartureBoard: (String, String) -> Unit) {
    composable<LiveTrains> {
        LiveTrainsScreen {
            navigateToDepartureBoard("WAT", "London Waterloo")
        }
    }
}