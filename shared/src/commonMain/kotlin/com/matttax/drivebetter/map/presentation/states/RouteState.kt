package com.matttax.drivebetter.map.presentation.states

import com.matttax.drivebetter.map.routes.model.Route

sealed interface RouteState {

    data object NoSearch : RouteState

    data object Loading : RouteState

    data object Empty : RouteState

    data class Error(val message: String) : RouteState

    data class Results(val list: List<Route>) : RouteState
}
