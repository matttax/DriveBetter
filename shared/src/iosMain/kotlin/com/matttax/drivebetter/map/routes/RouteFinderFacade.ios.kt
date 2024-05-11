package com.matttax.drivebetter.map.routes

import com.matttax.drivebetter.map.routes.model.Route
import kotlinx.coroutines.flow.StateFlow

actual class RouteFinderFacade actual constructor() {
    actual val routes: StateFlow<Result<List<Route>>>
        get() = TODO("Not yet implemented")

    actual fun submitRequest(request: RouteSearchRequest) {
    }

    actual fun cancelRequest() {}

    actual fun refresh() {
    }
}
