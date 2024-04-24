package com.matttax.drivebetter.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.matttax.drivebetter.R
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Circle
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

@Composable
actual fun YandexMapView() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapView = remember { MapView(context) }
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
            mapView.apply {
                mapWindow.map.move(
                    CameraPosition(
                        Point(55.641614, 37.530480),
                        20.0f,
                        0.0f,
                        0.0f
                    )
                )
                val circle = Circle(
                    Point(55.641614, 37.530480),
                    10f
                )
                mapWindow.map.mapObjects.addCircle(circle).apply {
                    strokeWidth = 10f
                    strokeColor = ContextCompat.getColor(context, R.color.red_alpha)
                    fillColor = ContextCompat.getColor(context, R.color.red)
                }
            }
        },
        update = {

        }
    )
}
