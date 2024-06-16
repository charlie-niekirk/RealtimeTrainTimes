package me.cniekirk.realtimetrains.feature.departureboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import me.cniekirk.realtimetrains.core.common.Direction
import me.cniekirk.realtimetrains.core.data.model.ArrivalBoardTrainService
import me.cniekirk.realtimetrains.core.data.model.DepartureBoardTrainService
import me.cniekirk.realtimetrains.core.data.model.StationBoard
import me.cniekirk.realtimetrains.core.data.model.TimeStatus
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

@Composable
fun DepartureBoardContent(
    state: DepartureBoardState,
    onServiceClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.surface),
    ) {
        val heading = when (state.direction) {
            Direction.DEPARTING -> stringResource(id = R.string.departures)
            Direction.ARRIVING -> stringResource(id = R.string.arrivals)
        }

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 32.dp),
            text = state.stationName,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Normal
        )

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 4.dp),
            text = heading,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        AnimatedContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
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
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    when (val board = state.stationBoard) {
                        is StationBoard.ArrivalBoard -> {
                            ArrivalBoardServices(services = board.services) {
                                onServiceClicked(it)
                            }
                        }
                        is StationBoard.DepartureBoard -> {
                            DepartureBoardServices(services = board.services) {
                                onServiceClicked(it)
                            }
                        }
                        null -> {}
                    }
                }
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
        items(services, contentType = { it.contentType }) { service ->
            DepartureTrainServiceItem(
                departureBoardTrainService = service,
                onServiceClicked = { onServiceClicked(service.id) }
            )
        }
        item {
            // Load more trains item
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                OutlinedButton(onClick = { /*TODO*/ }) {
                    Text(
                        text = stringResource(id = R.string.load_more)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun ArrivalBoardServices(
    services: ImmutableList<ArrivalBoardTrainService>,
    onServiceClicked: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(services, contentType = { it.contentType }) { service ->
            ArrivalTrainServiceItem(
                arrivalBoardTrainService = service,
                onServiceClicked = { onServiceClicked(service.id) }
            )
        }
        item {
            // Load more trains item
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                OutlinedButton(onClick = { /*TODO*/ }) {
                    Text(
                        text = stringResource(id = R.string.load_more)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun DepartureTrainServiceItem(
    departureBoardTrainService: DepartureBoardTrainService,
    onServiceClicked: () -> Unit
) {
    val modifier = if (departureBoardTrainService.hasDeparted) {
        Modifier
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
    } else Modifier

    Column(
        modifier = modifier.clickable { onServiceClicked() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
        ) {
            Column {
                val scheduledStyle = when (departureBoardTrainService.timeStatus) {
                    is TimeStatus.Late -> MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.LineThrough)
                    is TimeStatus.OnTime, is TimeStatus.Cancelled -> MaterialTheme.typography.bodyMedium
                }

                Text(
                    text = departureBoardTrainService.scheduledDepartureTime,
                    style = scheduledStyle,
                    fontWeight = FontWeight.Bold
                )

                if (departureBoardTrainService.timeStatus is TimeStatus.Late) {
                    Text(
                        modifier = Modifier.padding(top = 2.dp),
                        text = departureBoardTrainService.realtimeDepartureTime,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = departureBoardTrainService.trainOperator,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(departureBoardTrainService.trainOperatorColor)
                )

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = departureBoardTrainService.destination,
                    style = MaterialTheme.typography.bodyMedium
                )

                val timeString = when (val status = departureBoardTrainService.timeStatus) {
                    is TimeStatus.Late -> {
                        if (departureBoardTrainService.hasDeparted) {
                            pluralStringResource(id = R.plurals.departed_late_format, count = status.minutes, status.minutes)
                        } else {
                            pluralStringResource(id = R.plurals.late_format, count = status.minutes, status.minutes)
                        }
                    }
                    is TimeStatus.OnTime -> {
                        if (departureBoardTrainService.hasDeparted) {
                            stringResource(id = R.string.departed_on_time)
                        } else {
                            stringResource(id = R.string.on_time)
                        }
                    }
                    is TimeStatus.Cancelled -> {
                        stringResource(id = R.string.cancelled_format, status.reason)
                    }
                }

                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = timeString,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (departureBoardTrainService.timeStatus is TimeStatus.Cancelled) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.align(Alignment.CenterVertically),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = stringResource(id = R.string.platform_format, departureBoardTrainService.platform),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = if (departureBoardTrainService.platformConfirmed) FontWeight.Bold else FontWeight.Normal
                )
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = stringResource(id = departureBoardTrainService.trainLocation.status),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        HorizontalDivider()
    }
}

@Composable
fun ArrivalTrainServiceItem(
    arrivalBoardTrainService: ArrivalBoardTrainService,
    onServiceClicked: () -> Unit
) {
    val modifier = if (arrivalBoardTrainService.hasArrived) {
        Modifier
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
    } else Modifier

    Column(
        modifier = modifier.clickable { onServiceClicked() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Column {
                val scheduledStyle = when (arrivalBoardTrainService.timeStatus) {
                    is TimeStatus.Late -> MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.LineThrough)
                    is TimeStatus.OnTime, is TimeStatus.Cancelled -> MaterialTheme.typography.bodyMedium
                }

                Text(
                    text = arrivalBoardTrainService.scheduledArrivalTime,
                    style = scheduledStyle,
                    fontWeight = FontWeight.Bold
                )

                if (arrivalBoardTrainService.timeStatus is TimeStatus.Late) {
                    Text(
                        modifier = Modifier.padding(top = 2.dp),
                        text = arrivalBoardTrainService.realtimeArrivalTime,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = arrivalBoardTrainService.trainOperator,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(arrivalBoardTrainService.trainOperatorColor)
                )

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = arrivalBoardTrainService.origin,
                    style = MaterialTheme.typography.bodyMedium
                )

                val timeString = when (val status = arrivalBoardTrainService.timeStatus) {
                    is TimeStatus.Late -> {
                        if (arrivalBoardTrainService.hasArrived) {
                            pluralStringResource(id = R.plurals.arrived_late_format, count = status.minutes, status.minutes)
                        } else {
                            pluralStringResource(id = R.plurals.late_format, count = status.minutes, status.minutes)
                        }
                    }
                    is TimeStatus.OnTime -> {
                        if (arrivalBoardTrainService.hasArrived) {
                            stringResource(id = R.string.arrived_on_time)
                        } else {
                            stringResource(id = R.string.on_time)
                        }
                    }
                    is TimeStatus.Cancelled -> {
                        stringResource(id = R.string.cancelled_format, status.reason)
                    }
                }

                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = timeString,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (arrivalBoardTrainService.timeStatus is TimeStatus.Cancelled) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.align(Alignment.CenterVertically),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = stringResource(id = R.string.platform_format, arrivalBoardTrainService.platform),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = if (arrivalBoardTrainService.platformConfirmed) FontWeight.Bold else FontWeight.Normal
                )

                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = stringResource(id = arrivalBoardTrainService.trainLocation.status),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        HorizontalDivider()
    }
}