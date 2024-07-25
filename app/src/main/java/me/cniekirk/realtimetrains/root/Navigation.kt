package me.cniekirk.realtimetrains.root

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import me.cniekirk.realtimetrains.R
import me.cniekirk.realtimetrains.core.common.Direction
import me.cniekirk.realtimetrains.feature.departureboard.DepartureBoard
import me.cniekirk.realtimetrains.feature.departureboard.departureBoard
import me.cniekirk.realtimetrains.feature.livetrains.LiveTrains
import me.cniekirk.realtimetrains.feature.livetrains.liveTrains
import me.cniekirk.realtimetrains.feature.servicedetails.ServiceDetails
import me.cniekirk.realtimetrains.feature.servicedetails.serviceDetails
import me.cniekirk.realtimetrains.feature.stationsearch.StationSearch
import me.cniekirk.realtimetrains.feature.stationsearch.stationSearch
import kotlin.reflect.KClass
import kotlin.reflect.typeOf

//sealed class TabDestination(val label: String, val route: KClass<*>, val icon: ImageVector) {
//
//    data object LiveTrains : TabDestination(
//        label = "",
//        route = LiveTrains,
//        icon = Icons.Default.Train
//    )
//
//    data object Planner : TabDestination(
//        label = "Planner",
//        route = "",
//        icon = Icons.Default.CalendarMonth
//    )
//
//    data object Settings : TabDestination(
//        label = "Settings",
//        route = "settings",
//        icon = Icons.Default.Settings
//    )
//}



@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RealTimeTrainsNavHost(navHostController: NavHostController) {
    val tabs = listOf(
        stringResource(id = R.string.live_trains) to LiveTrains,
        stringResource(id = R.string.planner) to LiveTrains,
        stringResource(id = R.string.settings) to LiveTrains,
    )

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            val icon = when (index) {
                                0 -> {
                                    Icons.Default.Train
                                }
                                1 -> {
                                    Icons.Default.CalendarMonth
                                }
                                else -> {
                                    Icons.Default.Settings
                                }
                            }
                            Icon(icon, contentDescription = null)
                        },
                        label = { Text(item.first) },
                        selected = currentDestination?.hierarchy?.any {
                            it.hasRoute(item.second::class)
                        } == true,
                        onClick = {
                            navHostController.navigate(item.second) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navHostController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                // Restore state when re-selecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        SharedTransitionLayout {
            NavHost(
                modifier = Modifier.padding(paddingValues),
                navController = navHostController,
                startDestination = LiveTrains
            ) {
                liveTrains(
                    navHostController = navHostController,
                    slideAnimationRoute = StationSearch::class,
                    activityAnimationRoute = DepartureBoard::class,
                    navigateToDepartureBoard = { searchStation, filterStation, direction ->
                        navHostController.navigate(
                            DepartureBoard(
                                searchStationCrs = searchStation.crsCode,
                                searchStationName = searchStation.stationName,
                                filterStationCrs = filterStation?.crsCode,
                                filterStationName = filterStation?.stationName,
                                isDeparting = direction == Direction.DEPARTING
                            )
                        )
                    },
                    navigateToStationSearch = {
                        navHostController.navigate(StationSearch(it))
                    }
                )

                stationSearch(LiveTrains::class) { stationCrs, stationName, stationType ->
                    navHostController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("station_crs", stationCrs)
                    navHostController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("station_name", stationName)
                    navHostController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("station_type", stationType)
                    navHostController.popBackStack()
                }

                departureBoard(
                    animationRoute = LiveTrains::class,
                    navigateToServiceDetails = {
                        navHostController.navigate(ServiceDetails(it))
                    }
                )

                serviceDetails(
                    animationRoute = DepartureBoard::class
                )
            }
        }
    }
}