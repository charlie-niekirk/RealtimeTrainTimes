package me.cniekirk.realtimetrains.feature.servicedetails

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import me.cniekirk.realtimetrains.feature.servicedetails.components.StationList
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ServiceDetailsScreen(viewModel: ServiceDetailsViewModel) {
    val state = viewModel.collectAsState().value

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ServiceDetailsEffect.NavigateBack -> {

            }
            is ServiceDetailsEffect.DisplayError -> {

            }
        }
    }

    ServiceDetailsContent(state = state)
}

@Composable
fun ServiceDetailsContent(state: ServiceDetailsState) {
    AnimatedContent(
        targetState = state.isLoading,
        label = "Loading animation"
    ) { loading ->
        if (loading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surface),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            state.serviceDetails?.also { serviceDetails ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.surface),
                ) {
                    Text(
                        modifier = Modifier.padding(start = 16.dp, top = 32.dp),
                        text = serviceDetails.origin.first().description ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Normal
                    )

                    Text(
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                        text = "to",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )

                    Text(
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                        text = serviceDetails.destination.first().description ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Normal
                    )

                    StationList(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        stations = serviceDetails.locations.toImmutableList()
                    )
                }
            }
        }
    }
}