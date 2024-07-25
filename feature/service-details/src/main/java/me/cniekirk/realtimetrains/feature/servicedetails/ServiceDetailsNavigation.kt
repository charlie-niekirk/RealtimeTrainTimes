package me.cniekirk.realtimetrains.feature.servicedetails

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import me.cniekirk.realtimetrains.core.designsystem.activityDefaultEnter
import me.cniekirk.realtimetrains.core.designsystem.activityDefaultExit
import me.cniekirk.realtimetrains.core.designsystem.activityDefaultPopEnter
import me.cniekirk.realtimetrains.core.designsystem.activityDefaultPopExit
import me.cniekirk.realtimetrains.core.designsystem.emphasisedIntSpec
import kotlin.reflect.KClass

@Serializable
data class ServiceDetails(
    val serviceUid: String
)

fun NavGraphBuilder.serviceDetails(animationRoute: KClass<*>) {
    composable<ServiceDetails>(
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
        val viewModel = hiltViewModel<ServiceDetailsViewModel>()
        ServiceDetailsScreen(viewModel)
    }
}