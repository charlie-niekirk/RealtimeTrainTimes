package me.cniekirk.realtimetrains.feature.servicedetails.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.cniekirk.realtimetrains.core.designsystem.RealTimeTrainsTheme
import me.cniekirk.realtimetrains.core.network.models.LocationDetail

@Composable
fun StationList(
    modifier: Modifier = Modifier,
    stations: ImmutableList<LocationDetail>
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(stations) { index, item ->
            StationListItem(
                locationDetail = item,
                isFirst = index == 0,
                isLast = index == stations.lastIndex
            )
        }
    }
}

@Composable
private fun StationListItem(
    modifier: Modifier = Modifier,
    locationDetail: LocationDetail,
    isFirst: Boolean = false,
    isLast: Boolean = false
) {
    val drawColor = MaterialTheme.colorScheme.onSurface
    val stationColor = MaterialTheme.colorScheme.primary

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier
            .width(32.dp)
            .height(64.dp)) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val lineStartY = if (isFirst) {
                (canvasHeight / 2f) + 8
            } else 0f

            val lineEndY = if (isLast) {
                (canvasHeight / 2f) - 8
            } else canvasHeight

            drawLine(
                color = drawColor,
                start = Offset(canvasWidth / 2f, lineStartY),
                end = Offset(canvasWidth / 2f, lineEndY),
                strokeWidth = 2.dp.toPx()
            )

            drawCircle(
                color = stationColor,
                radius = 8.dp.toPx(),
                center = Offset(canvasWidth / 2f, canvasHeight / 2f)
            )
        }

        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = locationDetail.description ?: "",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = locationDetail.gbttBookedDeparture ?: "",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview
@Composable
private fun StationListPreview(@PreviewParameter(StationsParameterProvider::class) stations: ImmutableList<LocationDetail>) {
    RealTimeTrainsTheme {
        Surface {
            StationList(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                stations = stations
            )
        }
    }
}