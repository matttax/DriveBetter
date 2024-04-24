package com.matttax.drivebetter.history.presentation

import com.matttax.drivebetter.history.domain.model.Ride

sealed interface RideHistoryState {

    data object Loading : RideHistoryState

    data class Error(val message: String) : RideHistoryState

    data class RidesList(val rides: List<Ride>) : RideHistoryState

    data class RideDetails(val ride: Ride) : RideHistoryState
}
