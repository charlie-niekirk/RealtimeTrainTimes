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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import me.cniekirk.realtimetrains.feature.departureboard.DepartureBoard
import me.cniekirk.realtimetrains.feature.departureboard.departureBoard
import me.cniekirk.realtimetrains.feature.stationsearch.LiveTrains
import me.cniekirk.realtimetrains.feature.stationsearch.liveTrains

sealed class TabDestination(val label: String, val route: Any, val icon: ImageVector) {

    data object LiveTrains : TabDestination(
        label = "Live Trains",
        route = LiveTrains,
        icon = Icons.Default.Train
    )

    data object Planner : TabDestination(
        label = "Planner",
        route = "",
        icon = Icons.Default.CalendarMonth
    )

    data object Settings : TabDestination(
        label = "Settings",
        route = "settings",
        icon = Icons.Default.Settings
    )
}

val tabs = listOf(
    TabDestination.LiveTrains,
    TabDestination.Planner,
    TabDestination.Settings,
)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RealTimeTrainsNavHost(navHostController: NavHostController) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.icon.name) },
                        label = { Text(item.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navHostController.navigate(item.route) {
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
                startDestination = me.cniekirk.realtimetrains.feature.stationsearch.LiveTrains
            ) {
                liveTrains {  stationCrs, stationName ->
                    navHostController.navigate(DepartureBoard(stationCrs, stationName))
                }

                departureBoard { serviceUid ->
//                navHostController.navigate()
                }
            }
        }
    }
}