package com.matttax.drivebetter.history

import android.annotation.SuppressLint
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import co.yml.charts.common.model.Point
import com.matttax.drivebetter.network.model.Address
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RideViewModel @Inject constructor(
    private val rideRepository: DriveRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val rideId: Int = checkNotNull(savedStateHandle[RIDE_ID_KEY])
    val currentRide = rideRepository.getRideById(rideId)

    val divider: Int
        get() {
            return if (getStart() - getEnd() > 1.5 * 3_600_000L)
                3_600_000
            else 60_000
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
        return intervals
    }

    fun getWeather(): List<Weather> {
        return currentRide?.weatherHistory ?: emptyList()
    }

    fun getStart(): Long = currentRide?.weatherHistory?.firstOrNull()?.timestamp ?: System.currentTimeMillis()

    fun getEnd(): Long = currentRide?.weatherHistory?.lastOrNull()?.timestamp ?: System.currentTimeMillis()

    fun getDangerousData(): List<DangerousDriving> {
        if (currentRide == null) return emptyList()
        return (currentRide.accelerationHistory.map {
            DangerousDriving(DangerousDrivingType.ACCELERATION, it.address.short, it.timestamp)
        } + currentRide.shiftHistory.map {
            DangerousDriving(DangerousDrivingType.SHARP_TURN, it.address.short, it.timestamp)
        }).sortedBy { it.timestamp }
    }

    companion object {
        const val RIDE_ID_KEY = "id"
    }
}

data class WeatherInterval(
    val weatherType: WeatherType,
    val startTimestamp: Long,
    val endTimestamp: Long
)

object DateUtils {

    fun timestampToHourMinutes(timestamp: Long): String {
        return sfd.format(Date(timestamp))
    }

    @SuppressLint("SimpleDateFormat")
    private val sfd = SimpleDateFormat("HH:mm")
}

object MockDataProvider {

    private val time = System.currentTimeMillis() - 123_500_000L

    val mockWeatherData = listOf(
        Weather(time - 1_900_000L, 0.78f, 0),
        Weather(time - 1_810_000L, 0.74f, 0),
        Weather(time - 1_760_000L, 0.71f, 0),
        Weather(time - 1_520_000L, 0.69f, 0),
        Weather(time - 1_450_000L, 0.84f, 0),
        Weather(time - 1_400_000L, 0.78f, 0),
        Weather(time - 1_000_000L, 0.98f, 0),
        Weather(time - 980_000L, 0.98f, 0),
        Weather(time - 430_000L, 0.5f, 0),
        Weather(time - 370_000L, 0.55f, 0),
        Weather(time - 270_000L, 0.68f, 0),
        Weather(time - 250_000L, 0.87f, 0),
        Weather(time - 150_000L, 0.98f, 0),
    )

    val mockSpeeding = listOf(
        Speeding(time - 1_860_000L, 110.03f, Address(short = "ул. Вавилова, д. 46", full="", regionTypeFull="", regionTypeShort="")),
        Speeding(time - 810_000L, 90.7f, Address(short = "ул. Бутлерова, д. 4", full="", regionTypeFull="", regionTypeShort="")),
        Speeding(time - 510_000L, 81.9f, Address(short = "ул. Голубинская, д. 32/2", full="", regionTypeFull="", regionTypeShort=""))
    )

    val mockDangerousData = listOf(
        DangerousDriving(DangerousDrivingType.ACCELERATION, "ул. Большая Полянка, д. 1/3", time - 1_423_000L),
        DangerousDriving(DangerousDrivingType.SHARP_TURN, "ул. Паустовского, д. 8", time - 1_189_000L),
        DangerousDriving(DangerousDrivingType.SHARP_TURN, "ул. Голубинская, д. 32/2", time - 1_174_000L),
        DangerousDriving(DangerousDrivingType.ACCELERATION, "ул. Фитина, д. 2", time - 923_000L),
        DangerousDriving(DangerousDrivingType.ACCELERATION, "ул. Профсоюзная, д. 26", time - 423_000L),
    )

    val rating = 6.3f

    val start = mockWeatherData.minOf { it.timestamp }
    val divider = if (mockWeatherData.last().timestamp - mockWeatherData.first().timestamp > 1.5 * 3_600_000L)
        3_600_000
    else 60_000

    fun getWeather(): List<WeatherInterval> {
        return listOf(
            WeatherInterval(WeatherType.SUNNY, time - 1_000_000, time - 600_000),
            WeatherInterval(WeatherType.RAIN, time - 600_000, time - 400_000),
            WeatherInterval(WeatherType.THUNDERSTORM, time - 400_000, time - 150_000),
            WeatherInterval(WeatherType.SUNNY, time - 150_000, time - 0),
        )
    }
}