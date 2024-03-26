package com.matttax.drivebetter.network

import com.matttax.drivebetter.history.Drive
import com.matttax.drivebetter.history.Ride
import com.matttax.drivebetter.network.model.Address
import com.matttax.drivebetter.network.model.UserRideBatch
import com.matttax.drivebetter.network.model.UserRideInfo
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
): ApiHelper {

    override fun getLocation(userRideInfo: UserRideInfo): Flow<Address> {
        return flow {
            try {
                emit(apiService.getLocation(userRideInfo).address)
            } catch (ignore: Exception) { }
        }
    }

    override fun getRidesHistory(userId: String): Flow<List<Ride>> {
        return flow {
            try {
                emit(apiService.getRidesHistory(userId))
            } catch (ignore: Exception) { }
        }
    }

    override suspend fun sendBatch(userRideBatch: Drive) {
        try {
            apiService.sendBatch(userRideBatch)
        } catch (ex: Exception) {
            //ignore
        }
    }
}
