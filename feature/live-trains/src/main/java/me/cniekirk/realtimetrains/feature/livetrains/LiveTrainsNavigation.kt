package me.cniekirk.realtimetrains.feature.livetrains

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import me.cniekirk.realtimetrains.core.common.Direction
import me.cniekirk.realtimetrains.core.designsystem.activityDefaultExit
import me.cniekirk.realtimetrains.core.designsystem.activityDefaultPopEnter
import me.cniekirk.realtimetrains.core.network.models.huxley.StationCrs
import kotlin.reflect.KClass

@Serializable
object LiveTrains

fun NavGraphBuilder.liveTrains(
    navHostController: NavHostController,
    slideAnimationRoute: KClass<*>,
    activityAnimationRoute: KClass<*>,
    navigateToDepartureBoard: (StationCrs, StationCrs?, Direction) -> Unit,
    navigateToStationSearch: (String) -> Unit
) {
    composable<LiveTrains>(
        popEnterTransition = {
            if (initialState.destination.hasRoute(slideAnimationRoute)) {
                slideInHorizontally(initialOffsetX = { - it / 2 }) + fadeIn()
            } else if (initialState.destination.hasRoute(activityAnimationRoute)) {
                activityDefaultPopEnter()
            } else {
                fadeIn()
            }
        },
        exitTransition = {
            if (targetState.destination.hasRoute(slideAnimationRoute)) {
                slideOutHorizontally(targetOffsetX = { - it / 2 }) + fadeOut()
            } else if (initialState.destination.hasRoute(activityAnimationRoute)) {
                activityDefaultExit()
            } else {
                fadeOut()
            }
        }
    ) {
        val viewModel = hiltViewModel<LiveTrainsViewModel>()

        val stationCrs = navHostController.currentBackStackEntry?.savedStateHandle?.get<String>("station_crs")
        val stationName = navHostController.currentBackStackEntry?.savedStateHandle?.get<String>("station_name")
        val stationType = navHostController.currentBackStackEntry?.savedStateHandle?.get<String>("station_type")

        val filterStation = if (stationType != null && stationCrs != null && stationName != null && stationType.equals("filter", true)) {
            StationCrs(stationCrs, stationName)
        } else null

        val searchStation = if (stationType != null && stationCrs != null && stationName != null && stationType.equals("search", true)) {
            StationCrs(stationCrs, stationName)
        } else null

        LiveTrainsScreen(
            viewModel = viewModel,
            searchStation = searchStation,
            filterStation = filterStation,
            navigateToStationSearch = {
                navigateToStationSearch(if (it) "filter" else "search")
            },
            navigateToDepartureBoard = { search, filter, direction ->
                navigateToDepartureBoard(search, filter, direction)
            }
        )
    }
}