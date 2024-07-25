package me.cniekirk.realtimetrains.feature.servicedetails.components

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.cniekirk.realtimetrains.core.network.models.LocationDetail

class StationsParameterProvider : PreviewParameterProvider<ImmutableList<LocationDetail>> {

    override val values = sequenceOf(
        persistentListOf(
            start,
            middle,
            end
        )
    )

}

val start = LocationDetail(
    "",
    "London Waterloo",
    listOf(),
    "",
    "16:24",
    "",
    true,
    true,
    listOf(),
    "",
    false,
    true,
    true,
    "",
    false,
    "",
    false,
    "",
    "",
    "",
    "",
    "",
)

val middle = LocationDetail(
    "",
    "Salisbury",
    listOf(),
    "",
    "17:12",
    "",
    true,
    true,
    listOf(),
    "",
    false,
    true,
    true,
    "",
    false,
    "",
    false,
    "",
    "",
    "",
    "",
    "",
)

val end = LocationDetail(
    "",
    "Exeter St. Davids",
    listOf(),
    "",
    "18:35",
    "",
    true,
    true,
    listOf(),
    "",
    false,
    true,
    true,
    "",
    false,
    "",
    false,
    "",
    "",
    "",
    "",
    "",
)