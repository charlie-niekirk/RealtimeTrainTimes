package me.cniekirk.realtimetrains.feature.departureboard

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import timber.log.Timber

@Serializable
data class DepartureBoard(
    val stationCrs: String,
    val stationName: String
)

fun NavGraphBuilder.departureBoard(navigateToServiceDetails: (String) -> Unit) {
    composable<DepartureBoard>(
        popEnterTransition = {
            Timber.d(targetState.destination.route)
            fadeIn()
        },
        exitTransition = {
            Timber.d(initialState.destination.route)
            fadeOut()
        }
    ) {
        val viewModel = hiltViewModel<DepartureBoardViewModel>()
        DepartureBoardScreen(
            viewModel = viewModel,
            navigateToServiceDetails = { navigateToServiceDetails(it) }
        )
    }
}