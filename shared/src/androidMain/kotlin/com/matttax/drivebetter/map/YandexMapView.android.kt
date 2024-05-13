package com.matttax.drivebetter.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.VectorDrawable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.matttax.drivebetter.R
import com.matttax.drivebetter.map.domain.model.GeoPoint
import com.matttax.drivebetter.map.domain.model.RidePoint
import com.matttax.drivebetter.map.domain.model.SearchItem
import com.matttax.drivebetter.map.domain.model.isCloseTo
import com.matttax.drivebetter.map.presentation.mapkit.CameraManager
import com.matttax.drivebetter.map.presentation.states.MapViewState
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Composable
actual fun YandexMapView(
    locationFlow: StateFlow<RidePoint?>,
    selectedItemFlow: StateFlow<SearchItem?>,
    selectedPolyline: StateFlow<Any?>,
    searchResultsFlow: Flow<List<SearchItem>>,
    onCreate: () -> Unit,
    onUpdate: (MapViewState) -> Unit,
    onDestinationSelected: (SearchItem) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapView = remember { MapView(context) }
    val cameraListener = remember {
        CameraListener { map, _, _, _ ->
            with(map.cameraPosition) {
                onUpdate(
                    MapViewState(
                        target.toGeoPoint(), azimuth, tilt, zoom
                    )
                )
            }
        }
    }
    val pointIconProvider = remember { ImageProvider.fromBitmap(R.drawable.baseline_location_pin_24.getBitmap(context)) }
    val selectedPointIconProvider = remember { ImageProvider.fromBitmap(R.drawable.baseline_location_pin_selected_24.getBitmap(context)) }
    val locationPointProvider = remember { ImageProvider.fromBitmap(R.drawable.baseline_circle_24.getBitmap(context)) }
    val ridePoint by locationFlow.collectAsState()
    val selectedResult by selectedItemFlow.collectAsState()
    val results by searchResultsFlow.collectAsState(emptyList())
    val polyline by selectedPolyline.collectAsState()
    var previousLocation by remember { mutableStateOf<GeoPoint?>(null) }
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
            it.mapWindow.map.apply {
                removeCameraListener(cameraListener)
                addCameraListener(cameraListener)
                mapObjects.clear()
                if (ridePoint?.location?.isCloseTo(previousLocation)?.not() == true) {
                    move(CameraManager.getPosition(ridePoint).toCameraPosition())
                }
                ridePoint?.location?.toMapkitPoint()?.let { point ->
                    mapObjects.addPlacemark().apply {
                        geometry = point
                        setIcon(locationPointProvider)
                    }
                }
                results.forEach { result ->
                    mapObjects.addPlacemark().apply {
                        geometry = result.point.toMapkitPoint()
                        if (result == selectedResult) {
                            setIcon(
                                selectedPointIconProvider,
                                IconStyle().apply {
                                    scale = 2f
                                }
                            )
                        } else {
                            setIcon(
                                pointIconProvider,
                                IconStyle().apply {
                                    scale = 1.5f
                                }
                            )
                        }
                        addTapListener { _, _ ->
                            onDestinationSelected(result)
                            true
                        }
                    }
                }
                (polyline as? Polyline)?.let { line ->
                    mapObjects.addPolyline(line).apply {
                        strokeWidth = 5f
                        outlineWidth = 1f
                        outlineColor = ContextCompat.getColor(context, R.color.black)
                        setStrokeColor(ContextCompat.getColor(context, R.color.blue))
                    }
                }
            }
            previousLocation = ridePoint?.location
        }
    )
}

fun Point.toGeoPoint() = GeoPoint(
    latitude, longitude
)

fun GeoPoint.toMapkitPoint() = Point(
    latitude, longitude
)

fun MapViewState.toCameraPosition() = CameraPosition(
    targetPoint.toMapkitPoint(), azimuth, tilt, zoom
)

private fun Int.getBitmap(context: Context): Bitmap {
    return (ResourcesCompat.getDrawable(context.resources, this, null) as VectorDrawable).toBitmap()
}
