package me.cniekirk.realtimetrains.feature.stationsearch

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.cniekirk.realtimetrains.core.network.models.huxley.StationCrs
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect


@Composable
fun StationSearchScreen(
    viewModel: StationSearchViewModel,
    stationSelected: (String, String, String) -> Unit
) {
    val state = viewModel.collectAsState().value

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is StationSearchEffect.StationSelected -> {
                stationSelected(sideEffect.stationCrs, sideEffect.stationName, sideEffect.stationType)
            }
        }
    }

    StationSearchContent(
        state = state,
        onQueryChanged = viewModel::queryStations,
        onStationSelected = viewModel::stationSelected,
    )
}

@Composable
fun StationSearchContent(
    state: StationSearchState,
    onQueryChanged: (String) -> Unit,
    onStationSelected: (StationCrs) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.surface),
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 32.dp),
            text = stringResource(id = R.string.station_search_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Normal
        )

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 4.dp),
            text = stringResource(id = R.string.station_search_subtitle),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            value = state.searchQuery,
            onValueChange = { onQueryChanged(it) },
            placeholder = { Text(text = stringResource(id = R.string.query_placeholder)) },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(state.trainStations) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onStationSelected(it) }
                        .animateItem()
                ) {
                    ListItem(
                        headlineContent = { Text(text = it.stationName) },
                        supportingContent = { Text(text = it.crsCode) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}