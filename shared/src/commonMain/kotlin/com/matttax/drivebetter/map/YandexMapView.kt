package com.matttax.drivebetter.map

import androidx.compose.runtime.Composable
import com.matttax.drivebetter.map.domain.GeoPoint
import com.matttax.drivebetter.map.domain.SearchItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Composable
expect fun YandexMapView(
    locationFlow: StateFlow<GeoPoint?>,
    selectedItemFlow: StateFlow<SearchItem?>,
    searchResultsFlow: Flow<List<SearchItem>>,
    onCreate: () -> Unit,
    onUpdate: (MapViewState) -> Unit,
    onDestinationSelected: (SearchItem) -> Unit
)

data class MapViewState(
    val targetPoint: GeoPoint,
    val azimuth: Float,
    val tilt: Float,
    val zoom: Float
)
