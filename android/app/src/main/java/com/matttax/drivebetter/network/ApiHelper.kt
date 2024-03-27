package com.matttax.drivebetter.network

import com.matttax.drivebetter.history.data.model.Drive
import com.matttax.drivebetter.history.domain.Ride
import com.matttax.drivebetter.network.model.Address
import com.matttax.drivebetter.network.model.UserRideInfo
import kotlinx.coroutines.flow.Flow

interface ApiHelper {
    fun getLocation(userRideInfo: UserRideInfo): Flow<Address>
    fun getRidesHistory(userId: String): Flow<List<Ride>>
    suspend fun sendBatch(userRideBatch: Drive)
}
