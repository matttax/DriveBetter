package com.matttax.drivebetter.map.routes

import com.matttax.drivebetter.map.routes.model.Route
import com.matttax.drivebetter.map.routes.model.RouteMetadata
import com.matttax.drivebetter.map.toMapkitPoint
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouterType
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.runtime.Error
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.lighthousegames.logging.KmLog

actual class RouteFinderFacade actual constructor() {

    private var currentSession: DrivingSession? = null
    private val _routes = MutableStateFlow<Result<List<Route>>>(Result.success(emptyList()))
    private val drivingRouter = DirectionsFactory.getInstance().createDrivingRouter(DrivingRouterType.COMBINED)
    private val vehicleOptions = VehicleOptions()
    private val drivingOptions = DrivingOptions().apply {
        routesCount = 12
    }
    private val drivingRouteListener = object : DrivingSession.DrivingRouteListener {
        override fun onDrivingRoutes(drivingRoutes: MutableList<DrivingRoute>) {
            _routes.update {
                Result.success(
                    drivingRoutes.map { drivingRoute ->
                        Route(
                            metadata = RouteMetadata(
                                distance = drivingRoute.metadata.weight.distance.text,
                                time = drivingRoute.metadata.weight.time.text,
                                timeWithTraffic = drivingRoute.metadata.weight.timeWithTraffic.text,
                                railwayCrossingCount = drivingRoute.railwayCrossings.size,
                                pedestrianCrossingCount = drivingRoute.pedestrianCrossings.size
                            ),
                            polyline = drivingRoute.geometry,
                            speedLimits = drivingRoute.speedLimits
                        )
                    }
                )
            }
        }

        override fun onDrivingRoutesError(error: Error) {
            val message = error.javaClass.name
            log.e { message }
            _routes.update {
                Result.failure(RuntimeException(message))
            }
        }
    }

    actual val routes: StateFlow<Result<List<Route>>>
        get() = _routes

    actual fun submitRequest(request: RouteSearchRequest) {
        currentSession = drivingRouter.requestRoutes(
            buildList {
                add(RequestPoint(request.from.toMapkitPoint(), RequestPointType.WAYPOINT, null, null))
                request.intermediate.map { it.toMapkitPoint() }.forEach {
                    add(RequestPoint(it, RequestPointType.WAYPOINT, null, null))
                }
                add(RequestPoint(request.to.toMapkitPoint(), RequestPointType.WAYPOINT, null, null))
            },
            drivingOptions,
            vehicleOptions,
            drivingRouteListener
        )
    }

    actual fun cancelRequest() {
        currentSession?.cancel()
    }

    actual fun refresh() {
        currentSession?.retry(drivingRouteListener)
    }

    companion object {
        private val log = KmLog("RouteFinderFacade")
    }
}
