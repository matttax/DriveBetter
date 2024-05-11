package com.matttax.drivebetter.map.routes

import com.matttax.drivebetter.map.routes.model.Route
import kotlinx.coroutines.flow.StateFlow

expect class RouteFinderFacade() {
    val routes: StateFlow<Result<List<Route>>>
    fun submitRequest(request: RouteSearchRequest)
    fun cancelRequest()
    fun refresh()
}
