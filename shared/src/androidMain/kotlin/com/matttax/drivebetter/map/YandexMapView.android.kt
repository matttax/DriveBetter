package com.matttax.drivebetter.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.matttax.drivebetter.map.domain.GeoPoint
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import kotlinx.coroutines.flow.StateFlow

@Composable
actual fun YandexMapView(locationFlow: StateFlow<GeoPoint?>, onCreate: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapView = remember { MapView(context) }
    val location by locationFlow.collectAsState()
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    MapKitFactory.initialize(context)
                }
                Lifecycle.Event.ON_START -> {
                    MapKitFactory.getInstance().onStart()
                    mapView.onStart()
                }
                Lifecycle.Event.ON_STOP -> {
                    MapKitFactory.getInstance().onStop()
                    mapView.onStop()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            onCreate()
            mapView
        },
        update = {
            mapView.mapWindow.map.move(MapWindowProvider.get(location))
        }
    )
}

fun Point.toGeoPoint() = GeoPoint(
    latitude, longitude
)

fun GeoPoint.toMapkitPoint() = Point(
    latitude, longitude
)

object MapWindowProvider {

    fun get(location: GeoPoint?): CameraPosition {
        return if (location == null) {
            CameraPosition(
                RUSSIA_CENTER_POINT,
                0.0f,
                0.0f,
                0.0f
            )
        } else {
            CameraPosition(
                location.toMapkitPoint(),
                15.0f,
                0.0f,
                0.0f
            )
        }
    }

    private val RUSSIA_CENTER_POINT = Point(60.0, 60.0)
}
