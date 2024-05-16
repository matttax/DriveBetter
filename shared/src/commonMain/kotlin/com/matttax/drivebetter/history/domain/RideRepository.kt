package com.matttax.drivebetter.history.domain

import com.matttax.drivebetter.history.domain.model.Ride

interface RideRepository {

    suspend fun getRideHistory(): Result<List<Ride>>

    suspend fun getRideById(id: Int): Result<Ride?>

}
