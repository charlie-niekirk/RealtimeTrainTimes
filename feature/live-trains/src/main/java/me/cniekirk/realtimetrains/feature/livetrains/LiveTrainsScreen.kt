package me.cniekirk.realtimetrains.feature.livetrains

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LiveTrainsScreen(
    viewModel: LiveTrainsViewModel,
    navigateToStationSearch: (Boolean) -> Unit,
    navigateToDepartureBoard: (String, String) -> Unit
) {
    val state = viewModel.collectAsState().value

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LiveTrainsEffect.NavigateToStationSearch -> {
                navigateToStationSearch(sideEffect.isFilterStation)
            }
            is LiveTrainsEffect.NavigateToDepartureBoard -> {
                navigateToDepartureBoard(sideEffect.stationCrs, sideEffect.stationName)
            }
        }
    }

    LiveTrainsContent(
        state = state,
        searchStationClicked = viewModel::searchStationClicked,
        filterStationClicked = viewModel::filterStationClicked,
        searchClicked = viewModel::searchClicked
    )
}

@Composable
fun LiveTrainsContent(
    state: LiveTrainsState,
    searchStationClicked: () -> Unit,
    filterStationClicked: () -> Unit,
    searchClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

    }
}