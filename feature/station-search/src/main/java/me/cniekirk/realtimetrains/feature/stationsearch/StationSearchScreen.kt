package me.cniekirk.realtimetrains.feature.stationsearch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import me.cniekirk.realtimetrains.core.network.models.huxley.StationCrs
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect


@Composable
fun StationSearchScreen(
    viewModel: StationSearchViewModel,
    goToBoard: (String, String) -> Unit
) {
    val state = viewModel.collectAsState().value

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LiveTrainsEffect.NavigateToDepartureBoard -> {
                goToBoard(sideEffect.stationCrs, sideEffect.stationName)
            }
        }
    }

    StationSearchContent(
        state = state,
        onExpandedChanged = viewModel::searchExpandedChanged,
        onQueryChanged = viewModel::queryStations,
        onStationSelected = viewModel::stationSelected
    )
}

@Composable
fun StationSearchContent(
    state: LiveTrainsState,
    onExpandedChanged: (Boolean) -> Unit,
    onQueryChanged: (String) -> Unit,
    onStationSelected: (StationCrs) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

    }
}