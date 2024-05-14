package me.cniekirk.realtimetrains.feature.departureboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import me.cniekirk.realtimetrains.core.data.model.DepartureBoardTrainService
import me.cniekirk.realtimetrains.core.designsystem.emphasisedFloatSpec
import me.cniekirk.realtimetrains.core.designsystem.emphasisedIntSpec
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun DepartureBoardScreen(
    viewModel: DepartureBoardViewModel,
    navigateToServiceDetails: (String) -> Unit
) {
    val state = viewModel.collectAsState().value

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is DepartureBoardEffect.NavigateToServiceDetails -> {
                navigateToServiceDetails(sideEffect.serviceId)
            }
        }
    }

    DepartureBoardContent(
        state = state,
        onServiceClicked = viewModel::onServiceClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepartureBoardContent(
    state: DepartureBoardState,
    onServiceClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = state.stationName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Normal
                )
            }
        )
        AnimatedContent(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            targetState = state.isLoading,
            transitionSpec = {
                fadeIn(animationSpec = emphasisedFloatSpec) + slideInVertically(animationSpec = emphasisedIntSpec,
                    initialOffsetY = { fullHeight -> fullHeight / 4 }) togetherWith
                        fadeOut(emphasisedFloatSpec)
            },
            label = "departure_board_loading_appear_dismiss"
        ) { loading ->
            if (loading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.weight(1f))
                }
            } else {
                DepartureBoardServices(
                    services = state.services,
                    onServiceClicked = { onServiceClicked(it) }
                )
            }
        }
    }
}

@Composable
fun DepartureBoardServices(
    services: ImmutableList<DepartureBoardTrainService>,
    onServiceClicked: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(services) { service ->
            TrainServiceItem(
                departureBoardTrainService = service,
                onServiceClicked = { onServiceClicked(service.id) }
            )
        }
    }
}

@Composable
fun TrainServiceItem(
    departureBoardTrainService: DepartureBoardTrainService,
    onServiceClicked: () -> Unit
) {
    val platform = if (departureBoardTrainService.platformConfirmed) {
        "Platform ${departureBoardTrainService.platform}"
    } else {
        "Platform ${departureBoardTrainService.platform} estimated"
    }

    Column(
        modifier = Modifier.clickable { onServiceClicked() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Column {
                Text(
                    text = departureBoardTrainService.destination,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = platform,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = departureBoardTrainService.realtimeDepartureTime,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }

        HorizontalDivider()
    }
}