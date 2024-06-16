package me.cniekirk.realtimetrains.feature.livetrains

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import me.cniekirk.realtimetrains.core.common.Direction
import me.cniekirk.realtimetrains.core.common.RecentTrainSearch
import me.cniekirk.realtimetrains.core.designsystem.RealTimeTrainsTheme
import me.cniekirk.realtimetrains.core.designsystem.components.ClickableCard
import me.cniekirk.realtimetrains.core.network.models.huxley.StationCrs
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LiveTrainsScreen(
    viewModel: LiveTrainsViewModel,
    searchStation: StationCrs?,
    filterStation: StationCrs?,
    navigateToStationSearch: (Boolean) -> Unit,
    navigateToDepartureBoard: (StationCrs, StationCrs?, Direction) -> Unit
) {
    val state = viewModel.collectAsState().value

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LiveTrainsEffect.NavigateToStationSearch -> {
                navigateToStationSearch(sideEffect.isFilterStation)
            }
            is LiveTrainsEffect.NavigateToDepartureBoard -> {
                navigateToDepartureBoard(sideEffect.searchStation, sideEffect.filterStation, sideEffect.direction)
            }
        }
    }

    LiveTrainsContent(
        state = state,
        searchStationClicked = viewModel::searchStationClicked,
        filterStationClicked = viewModel::filterStationClicked,
        departingClicked = viewModel::departingClicked,
        arrivingClicked = viewModel::arrivingClicked,
        searchClicked = viewModel::searchClicked,
        clearSearchClicked = viewModel::searchStationCleared,
        clearFilterClicked = viewModel::filterStationCleared,
        onRecentSearchClicked = viewModel::recentSearchClicked
    )

    LaunchedEffect(key1 = Unit) {
        if (filterStation != null) {
            viewModel.changeFilterStation(filterStation)
        }
        if (searchStation != null) {
            viewModel.changeSearchStation(searchStation)
        }
    }
}

@Composable
fun LiveTrainsContent(
    state: LiveTrainsState,
    searchStationClicked: () -> Unit,
    filterStationClicked: () -> Unit,
    departingClicked: () -> Unit,
    arrivingClicked: () -> Unit,
    searchClicked: () -> Unit,
    clearSearchClicked: () -> Unit,
    clearFilterClicked: () -> Unit,
    onRecentSearchClicked: (RecentTrainSearch) -> Unit
) {
    val recentSearches = state.recentTrainSearches.collectAsState(persistentListOf())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 32.dp),
            text = stringResource(id = R.string.live_trains_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Normal
        )

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            SegmentedButton(
                selected = state.direction == Direction.DEPARTING,
                onClick = { departingClicked() },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
            ) {
                Text(text = stringResource(id = R.string.departing_direction))
            }

            SegmentedButton(
                selected = state.direction == Direction.ARRIVING,
                onClick = { arrivingClicked() },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
            ) {
                Text(text = stringResource(id = R.string.arriving_direction))
            }
        }

        val searchPlaceholder = if (state.direction == Direction.DEPARTING) stringResource(id = R.string.departing_search_station_container_text)
            else stringResource(id = R.string.arriving_search_station_container_text)

        val filterPlaceholder = if (state.direction == Direction.DEPARTING) stringResource(id = R.string.departing_filter_station_container_text)
            else stringResource(id = R.string.arriving_filter_station_container_text)

        ClickableCard(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            text = state.searchStation?.stationName,
            placeholder = searchPlaceholder,
            onClick = { searchStationClicked() },
            onLeadingButtonClick = { clearSearchClicked() }
        )

        ClickableCard(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
            text = state.filterStation?.stationName,
            placeholder = filterPlaceholder,
            onClick = { filterStationClicked() },
            onLeadingButtonClick = { clearFilterClicked() }
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            onClick = { searchClicked() },
        ) {
            Text(text = stringResource(id = R.string.search_button))
        }

        if (recentSearches.value.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(id = R.string.recent_searches_title),
                style = MaterialTheme.typography.labelSmall
            )

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(recentSearches.value) { recentSearch ->
                    RecentSearch(recentTrainSearch = recentSearch) {
                        onRecentSearchClicked(recentSearch)
                    }
                }
            }
        }
    }
}

@Composable
fun RecentSearch(
    modifier: Modifier = Modifier,
    recentTrainSearch: RecentTrainSearch,
    onRecentSearchClicked: (RecentTrainSearch) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.inverseSurface,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(shape = RoundedCornerShape(8.dp))
            .clickable { onRecentSearchClicked(recentTrainSearch) }
    ) {
        if (recentTrainSearch.filterName.isEmpty()) {
            Row(modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp)) {
                Text(
                    modifier = Modifier.alpha(0.5f),
                    text = if (recentTrainSearch.direction == Direction.DEPARTING) stringResource(id = R.string.from) else stringResource(
                        id = R.string.to
                    ),
                    style = MaterialTheme.typography.labelSmall
                )

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = recentTrainSearch.searchName,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        } else {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                text = recentTrainSearch.searchName,
                style = MaterialTheme.typography.labelSmall
            )

            Text(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp).alpha(0.5f),
                text = if (recentTrainSearch.direction == Direction.DEPARTING) stringResource(id = R.string.to) else stringResource(
                    id = R.string.from
                ),
                style = MaterialTheme.typography.labelSmall
            )

            Text(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 16.dp),
                text = recentTrainSearch.filterName,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview
@Composable
private fun RecentSearchPreview() {
    val recentSearch = RecentTrainSearch(
        direction = Direction.DEPARTING,
        searchName = "London Waterloo",
        searchCrs = "WAT",
        filterCrs = "SAL",
        filterName = ""
    )

    RealTimeTrainsTheme {
        Surface {
            RecentSearch(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                recentTrainSearch = recentSearch
            ) {}
        }
    }
}

@Preview
@Composable
fun LiveTrainsContentPreview() {
    val recentSearches = flowOf(
        persistentListOf(
            RecentTrainSearch(
                direction = Direction.DEPARTING,
                searchName = "London Waterloo",
                searchCrs = "WAT",
                filterCrs = "SAL",
                filterName = "Salisbury"
            )
        )
    )
    RealTimeTrainsTheme {
        Surface {
            LiveTrainsContent(
                state = LiveTrainsState(recentTrainSearches = recentSearches),
                searchStationClicked = {},
                filterStationClicked = {},
                departingClicked = {},
                arrivingClicked = {},
                searchClicked = {},
                clearSearchClicked = {},
                clearFilterClicked = {},
                onRecentSearchClicked = {}
            )
        }
    }
}