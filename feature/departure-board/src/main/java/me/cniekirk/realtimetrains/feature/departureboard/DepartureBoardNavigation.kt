package me.cniekirk.realtimetrains.feature.departureboard

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import me.cniekirk.realtimetrains.core.common.Direction
import me.cniekirk.realtimetrains.core.designsystem.activityDefaultEnter
import me.cniekirk.realtimetrains.core.designsystem.activityDefaultExit
import me.cniekirk.realtimetrains.core.designsystem.activityDefaultPopEnter
import me.cniekirk.realtimetrains.core.designsystem.activityDefaultPopExit
import kotlin.reflect.KClass
import kotlin.reflect.typeOf

@Serializable
data class DepartureBoard(
    val searchStationCrs: String,
    val searchStationName: String,
    val filterStationCrs: String?,
    val filterStationName: String?,
    val isDeparting: Boolean
)

fun NavGraphBuilder.departureBoard(
    navigateToServiceDetails: (String) -> Unit,
    animationRoute: KClass<*>
) {
    composable<DepartureBoard>(
        popEnterTransition = {
            if (targetState.destination.hasRoute(animationRoute)) {
                activityDefaultPopEnter()
            } else {
                fadeIn()
            }
        },
        exitTransition = {
            if (initialState.destination.hasRoute(animationRoute)) {
                activityDefaultExit()
            } else {
                fadeOut()
            }
        },
        enterTransition = {
            if (initialState.destination.hasRoute(animationRoute)) {
                activityDefaultEnter()
            } else {
                fadeIn()
            }
        },
        popExitTransition = {
            if (targetState.destination.hasRoute(animationRoute)) {
                activityDefaultPopExit()
            } else {
                fadeOut()
            }
        }
    ) {
        val viewModel = hiltViewModel<DepartureBoardViewModel>()
        DepartureBoardScreen(
            viewModel = viewModel,
            navigateToServiceDetails = { navigateToServiceDetails(it) }
        )
    }
}