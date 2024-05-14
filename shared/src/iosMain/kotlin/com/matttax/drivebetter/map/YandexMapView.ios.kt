package com.matttax.drivebetter.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import cocoapods.YandexMapsMobile.YMKCameraPosition
import cocoapods.YandexMapsMobile.YMKCameraUpdateReason
import cocoapods.YandexMapsMobile.YMKCircle.Companion.circleWithCenter
import cocoapods.YandexMapsMobile.YMKMap
import cocoapods.YandexMapsMobile.YMKMapCameraListenerProtocol
import cocoapods.YandexMapsMobile.YMKMapObject
import cocoapods.YandexMapsMobile.YMKMapObjectTapListenerProtocol
import cocoapods.YandexMapsMobile.YMKMapView
import cocoapods.YandexMapsMobile.YMKPoint
import cocoapods.YandexMapsMobile.YMKPolyline
import com.matttax.drivebetter.map.domain.model.GeoPoint
import com.matttax.drivebetter.map.domain.model.RidePoint
import com.matttax.drivebetter.map.domain.model.SearchItem
import com.matttax.drivebetter.map.domain.model.isCloseTo
import com.matttax.drivebetter.map.presentation.mapkit.CameraManager
import com.matttax.drivebetter.map.presentation.states.MapViewState
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import platform.UIKit.UIColor.Companion.blackColor
import platform.UIKit.UIColor.Companion.blueColor
import platform.UIKit.UIColor.Companion.greenColor
import platform.UIKit.UIColor.Companion.redColor
import platform.UIKit.UIColor.Companion.whiteColor
import platform.darwin.NSObject

@BetaInteropApi
@OptIn(ExperimentalForeignApi::class)
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
    val mapView = remember { YMKMapView() }
    val ridePoint by locationFlow.collectAsState()
    val selectedResult by selectedItemFlow.collectAsState()
    val results by searchResultsFlow.collectAsState(emptyList())
    val polyline by selectedPolyline.collectAsState()
    var previousLocation by remember { mutableStateOf<GeoPoint?>(null) }
    val cameraListener = remember {
        object : NSObject(), YMKMapCameraListenerProtocol {
            override fun onCameraPositionChangedWithMap(
                map: YMKMap,
                cameraPosition: YMKCameraPosition,
                cameraUpdateReason: YMKCameraUpdateReason,
                finished: Boolean
            ) {
                with(map.cameraPosition) {
                    onUpdate(
                        MapViewState(
                            target.toGeoPoint(), azimuth, tilt, zoom
                        )
                    )
                }
            }
        }
    }
    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            onCreate()
            mapView.also {
                it.mapWindow?.map?.addCameraListenerWithCameraListener(cameraListener)
            }
        },
        update = {
            it.mapWindow?.map?.apply {
                mapObjects.clear()
                if (ridePoint?.location?.isCloseTo(previousLocation)?.not() == true) {
                    moveWithCameraPosition(CameraManager.getPosition(ridePoint).toCameraPosition())
                }
                ridePoint?.location?.toMapkitPoint()?.let { point ->
                    mapObjects.addCircleWithCircle(
                        circleWithCenter(
                            point,
                            20f
                        ),
                        redColor,
                        20f,
                        whiteColor
                    )
                }
                results.forEach { result ->
                    if (result == selectedResult) {
                        mapObjects.addCircleWithCircle(
                            circleWithCenter(
                                result.point.toMapkitPoint(),
                                25f
                            ),
                            blueColor,
                            30f,
                            greenColor
                        )
                    } else {
                        mapObjects.addCircleWithCircle(
                            circleWithCenter(
                                result.point.toMapkitPoint(),
                                20f
                            ),
                            blueColor,
                            20f,
                            blueColor
                        )
                    }.apply {
                        addTapListenerWithTapListener(
                            object : NSObject(), YMKMapObjectTapListenerProtocol {
                                override fun onMapObjectTapWithMapObject(
                                    mapObject: YMKMapObject,
                                    point: YMKPoint
                                ): Boolean {
                                    onDestinationSelected(result)
                                    return true
                                }
                            }
                        )
                    }
                }
                (polyline as? YMKPolyline)?.let { line ->
                    mapObjects.addPolylineWithPolyline(line).apply {
                        strokeWidth = 5f
                        outlineWidth = 1f
                        outlineColor = blackColor
                        setStrokeColor(blueColor)
                    }
                }
            }
            previousLocation = ridePoint?.location
        }
    )
}

@OptIn(ExperimentalForeignApi::class)
fun YMKPoint.toGeoPoint() = GeoPoint(
    latitude, longitude
)

@OptIn(ExperimentalForeignApi::class)
fun GeoPoint.toMapkitPoint() = YMKPoint.pointWithLatitude(
    latitude, longitude
)

@OptIn(ExperimentalForeignApi::class)
fun MapViewState.toCameraPosition() = YMKCameraPosition.cameraPositionWithTarget(
    targetPoint.toMapkitPoint(), azimuth, tilt, zoom
)
