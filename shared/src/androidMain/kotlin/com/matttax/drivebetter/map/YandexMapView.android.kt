package com.matttax.drivebetter.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.VectorDrawable
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
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.matttax.drivebetter.R
import com.matttax.drivebetter.map.domain.GeoPoint
import com.matttax.drivebetter.map.domain.SearchItem
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Composable
actual fun YandexMapView(
    locationFlow: StateFlow<GeoPoint?>,
    selectedItemFlow: StateFlow<SearchItem?>,
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
    val location by locationFlow.collectAsState()
    val selectedResult by selectedItemFlow.collectAsState()
    val results by searchResultsFlow.collectAsState(emptyList())
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
//                move(MapWindowProvider.get(location))
                location?.toMapkitPoint()?.let { point ->
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
                                    scale = 3f
                                }
                            )
                        } else {
                            setIcon(
                                pointIconProvider,
                                IconStyle().apply {
                                    scale = 2f
                                }
                            )
                        }
                        addTapListener { _, _ ->
                            onDestinationSelected(result)
                            true
                        }
                    }
                }
            }
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

private fun Int.getBitmap(context: Context): Bitmap {
    return (ResourcesCompat.getDrawable(context.resources, this, null) as VectorDrawable).toBitmap()
}