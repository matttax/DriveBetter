package com.matttax.drivebetter.map

import androidx.compose.runtime.Composable
import com.matttax.drivebetter.map.domain.model.RidePoint
import com.matttax.drivebetter.map.domain.model.SearchItem
import com.matttax.drivebetter.map.presentation.states.MapViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Composable
expect fun YandexMapView(
    locationFlow: StateFlow<RidePoint?>,
    selectedItemFlow: StateFlow<SearchItem?>,
    selectedPolyline: StateFlow<Any?>,
    searchResultsFlow: Flow<List<SearchItem>>,
    onCreate: () -> Unit,
    onUpdate: (MapViewState) -> Unit,
    onDestinationSelected: (SearchItem) -> Unit
)
