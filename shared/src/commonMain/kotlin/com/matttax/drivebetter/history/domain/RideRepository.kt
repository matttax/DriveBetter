package com.matttax.drivebetter.history.domain

import com.matttax.drivebetter.history.domain.model.Ride

interface RideRepository {

    suspend fun getRideHistoryById(userId: String): List<Ride>

    suspend fun getRideById(id: Int): Ride?

}
