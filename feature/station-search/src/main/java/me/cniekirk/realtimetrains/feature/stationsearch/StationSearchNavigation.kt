package me.cniekirk.realtimetrains.feature.stationsearch

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable
data class StationSearch(val stationType: String)

fun NavGraphBuilder.stationSearch(
    slideInRoute: KClass<*>,
    stationSelected: (String, String, String) -> Unit
) {
    composable<StationSearch>(
        popEnterTransition = {
            if (targetState.destination.hasRoute(slideInRoute)) {
                slideInHorizontally(initialOffsetX = { - it / 2 }) + fadeIn()
            } else {
                fadeIn()
            }
        },
        exitTransition = {
            if (initialState.destination.hasRoute(slideInRoute)) {
                slideOutHorizontally(targetOffsetX = { - it / 2 }) + fadeOut()
            } else {
                fadeOut()
            }
        },
        enterTransition = {
            if (initialState.destination.hasRoute(slideInRoute)) {
                slideInHorizontally(initialOffsetX = { it / 2 }) + fadeIn()
            } else {
                fadeIn()
            }
        },
        popExitTransition = {
            if (targetState.destination.hasRoute(slideInRoute)) {
                slideOutHorizontally(targetOffsetX = { it / 2 }) + fadeOut()
            } else {
                fadeOut()
            }
        }
    ) {
        val viewModel = hiltViewModel<StationSearchViewModel>()
        StationSearchScreen(viewModel = viewModel) { stationCrs, stationName, stationType ->
            // Set the result
            stationSelected(stationCrs, stationName, stationType)
        }
    }
}