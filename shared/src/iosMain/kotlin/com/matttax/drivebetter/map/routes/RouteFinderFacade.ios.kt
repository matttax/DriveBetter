package com.matttax.drivebetter.map.routes

import cocoapods.YandexMapsMobile.YMKDirections
import cocoapods.YandexMapsMobile.YMKDrivingDrivingOptions
import cocoapods.YandexMapsMobile.YMKDrivingRoute
import cocoapods.YandexMapsMobile.YMKDrivingSession
import cocoapods.YandexMapsMobile.YMKDrivingSessionRouteHandler
import cocoapods.YandexMapsMobile.YMKDrivingVehicleOptions
import cocoapods.YandexMapsMobile.YMKRequestPoint
import cocoapods.YandexMapsMobile.YMKRequestPointType
import cocoapods.YandexMapsMobile.sharedInstance
import com.matttax.drivebetter.map.routes.model.Route
import com.matttax.drivebetter.map.routes.model.RouteMetadata
import com.matttax.drivebetter.map.toMapkitPoint
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.lighthousegames.logging.KmLog
import platform.Foundation.NSNumber

@OptIn(ExperimentalForeignApi::class)
actual class RouteFinderFacade actual constructor() {

    private var currentSession: YMKDrivingSession? = null
    private val _routes = MutableStateFlow<Result<List<Route>>>(Result.success(emptyList()))
    private val drivingRouter = YMKDirections.sharedInstance()?.createDrivingRouter()
    private val vehicleOptions = YMKDrivingVehicleOptions()
    private val drivingOptions = YMKDrivingDrivingOptions().apply {
        routesCount = NSNumber(12)
    }
    private val handler: YMKDrivingSessionRouteHandler = { drivingRoutes, error ->
        drivingRoutes?.mapNotNull { drivingRoute ->
            (drivingRoute as? YMKDrivingRoute)?.let {
                Route(
                    metadata = RouteMetadata(
                        distance = it.metadata.weight.distance.text,
                        time = it.metadata.weight.time.text,
                        timeWithTraffic = it.metadata.weight.timeWithTraffic.text,
                        railwayCrossingCount = it.fordCrossings.size,
                        pedestrianCrossingCount = drivingRoute.laneSigns.size
                    ),
                    polyline = drivingRoute.geometry,
                    speedLimits = drivingRoute.speedLimits.mapNotNull { limit ->
                        (limit as? NSNumber)?.floatValue
                    }
                )
            }
        }?.let { list ->
            _routes.update {
                Result.success(list)
            }
        } ?: run {
            val message = error?.localizedFailureReason ?: "Unknown error"
            log.e { "search error, $message" }
            _routes.update {
                Result.failure(RuntimeException(message))
            }
        }
    }

    actual val routes: StateFlow<Result<List<Route>>>
        get() = _routes

    actual fun submitRequest(request: RouteSearchRequest) {
        drivingRouter?.requestRoutesWithPoints(
            buildList {
                add(
                    YMKRequestPoint.requestPointWithPoint(request.from.toMapkitPoint(), YMKRequestPointType.YMKRequestPointTypeWaypoint, null)
                )
                request.intermediate.map { it.toMapkitPoint() }.forEach {
                    add(YMKRequestPoint.requestPointWithPoint(it, YMKRequestPointType.YMKRequestPointTypeWaypoint, null))
                }
                add(
                    YMKRequestPoint.requestPointWithPoint(request.to.toMapkitPoint(), YMKRequestPointType.YMKRequestPointTypeWaypoint, null)
                )
            },
            drivingOptions,
            vehicleOptions,
            handler
        )
    }

    actual fun cancelRequest() {
        currentSession?.cancel()
    }

    actual fun refresh() {
        currentSession?.retryWithRouteHandler(handler)
    }

    companion object {
        private val log = KmLog("RouteFinderFacade")
    }
}
