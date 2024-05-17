package com.matttax.drivebetter.history.presentation

import com.matttax.drivebetter.history.domain.RideRepository
import com.matttax.drivebetter.history.domain.model.Ride
import com.matttax.drivebetter.history.domain.model.Weather
import com.matttax.drivebetter.history.presentation.model.DangerousDriving
import com.matttax.drivebetter.history.presentation.model.DangerousDrivingType
import com.matttax.drivebetter.history.presentation.model.WeatherInterval
import com.matttax.drivebetter.history.presentation.model.WeatherType
import com.matttax.drivebetter.profile.data.token.LoginStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class RidesHistoryViewModel(
    private val rideRepository: RideRepository,
    private val loginStorage: LoginStorage
) : ViewModel() {

    private val _viewState = MutableStateFlow<RideHistoryState>(RideHistoryState.Loading)
    val viewState = _viewState.asStateFlow()

    private var _currentRide: Ride? = null
    val currentRide: Ride?
        get() = _currentRide

    fun getRides() {
        if (loginStorage.token == null) {
            _viewState.value = RideHistoryState.Unauthorized
        } else {
            _viewState.value = RideHistoryState.Loading
            viewModelScope.launch {
                val result = rideRepository.getRideHistory()
                _viewState.value = if (result.isSuccess) {
                    RideHistoryState.RidesList(result.getOrThrow())
                } else if (result.getOrNull()?.isEmpty() == true) {
                    RideHistoryState.Error("No rides found")
                } else {
                    RideHistoryState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            }
        }
    }

    fun getRideData(id: Int) {
        _viewState.update {
            if (it is RideHistoryState.RidesList) {
                _currentRide = it.rides[id]
                RideHistoryState.RideDetails(it.rides[id])
            } else it
        }
    }

    val startTimestamp: Long
        get() = _currentRide?.weatherHistory?.firstOrNull()?.timestamp ?: Clock.System.now().epochSeconds

    val endTimestamp: Long
        get() = _currentRide?.weatherHistory?.lastOrNull()?.timestamp ?: Clock.System.now().epochSeconds

    val divider: Int
        get() {
            return if (startTimestamp - endTimestamp > 1.5 * ONE_HOUR_MS)
                ONE_HOUR_MS
            else ONE_MINUTE_MS
        }

    fun getWeatherIntervals(): List<WeatherInterval> {
        val currentRide = _currentRide
        if (currentRide == null || currentRide.weatherHistory.isEmpty()) return emptyList()
        val intervals = mutableListOf<WeatherInterval>()
        var startTimestamp: Long = currentRide.weatherHistory[0].timestamp
        var currentWeatherCode: Int? = null
        for (weather in currentRide.weatherHistory) {
            if (weather.weatherCode != currentWeatherCode) {
                currentWeatherCode = weather.weatherCode
                intervals.add(
                    WeatherInterval(
                        startTimestamp = startTimestamp,
                        endTimestamp = weather.timestamp,
                        weatherType = WeatherType.SUNNY,
                    )
                )
                startTimestamp = weather.timestamp
            }
        }
        intervals.add(
            WeatherInterval(
                startTimestamp = startTimestamp,
                endTimestamp = currentRide.weatherHistory.last().timestamp,
                weatherType = WeatherType.SUNNY,
            )
        )
        return intervals
    }

    fun getDangerousData(): List<DangerousDriving> {
        val currentRide = _currentRide ?: return emptyList()
        return (currentRide.accelerationHistory.map {
            DangerousDriving(DangerousDrivingType.ACCELERATION, it.address.short, it.timestamp)
        } + currentRide.shiftHistory.map {
            DangerousDriving(DangerousDrivingType.SHARP_TURN, it.address.short, it.timestamp)
        }).sortedBy { it.timestamp }
    }

    fun getWeather(): List<Weather> {
        return _currentRide?.weatherHistory ?: emptyList()
    }

    companion object {
        const val RIDE_ID_KEY = "id"
        private const val ONE_HOUR_MS = 3_600_000
        private const val ONE_MINUTE_MS = 60_000
    }
}
