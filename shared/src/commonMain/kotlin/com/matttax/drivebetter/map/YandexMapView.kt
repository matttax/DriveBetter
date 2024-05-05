package com.matttax.drivebetter.map

import androidx.compose.runtime.Composable
import com.matttax.drivebetter.map.domain.GeoPoint
import kotlinx.coroutines.flow.StateFlow

@Composable
expect fun YandexMapView(
    locationFlow: StateFlow<GeoPoint?>,
    onCreate: () -> Unit
)
