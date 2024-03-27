package com.matttax.drivebetter.history.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.matttax.drivebetter.history.data.DriveRepository
import com.matttax.drivebetter.history.domain.Weather
import com.matttax.drivebetter.history.presentation.model.DangerousDriving
import com.matttax.drivebetter.history.presentation.model.DangerousDrivingType
import com.matttax.drivebetter.history.presentation.model.WeatherInterval
import com.matttax.drivebetter.history.presentation.model.WeatherType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RideViewModel @Inject constructor(
    private val rideRepository: DriveRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val rideId: Int = checkNotNull(savedStateHandle[RIDE_ID_KEY])
    val currentRide = rideRepository.getRideById(rideId)

    val startTimestamp: Long
        get() = currentRide?.weatherHistory?.firstOrNull()?.timestamp ?: System.currentTimeMillis()

    val endTimestamp: Long
        get() = currentRide?.weatherHistory?.lastOrNull()?.timestamp ?: System.currentTimeMillis()

    val divider: Int
        get() {
            return if (startTimestamp - endTimestamp > 1.5 * ONE_HOUR_MS)
                ONE_HOUR_MS
            else ONE_MINUTE_MS
        }

    fun getWeatherIntervals(): List<WeatherInterval> {
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
        if (currentRide == null) return emptyList()
        return (currentRide.accelerationHistory.map {
            DangerousDriving(DangerousDrivingType.ACCELERATION, it.address.short, it.timestamp)
        } + currentRide.shiftHistory.map {
            DangerousDriving(DangerousDrivingType.SHARP_TURN, it.address.short, it.timestamp)
        }).sortedBy { it.timestamp }
    }

    fun getWeather(): List<Weather> {
        return currentRide?.weatherHistory ?: emptyList()
    }

    companion object {
        const val RIDE_ID_KEY = "id"
        private const val ONE_HOUR_MS = 3_600_000
        private const val ONE_MINUTE_MS = 60_000
    }
}
