package com.matttax.drivebetter.history.data.mock

import com.matttax.drivebetter.history.domain.Speeding
import com.matttax.drivebetter.history.domain.Weather
import com.matttax.drivebetter.history.presentation.model.DangerousDriving
import com.matttax.drivebetter.history.presentation.model.DangerousDrivingType
import com.matttax.drivebetter.history.presentation.model.WeatherInterval
import com.matttax.drivebetter.history.presentation.model.WeatherType
import com.matttax.drivebetter.network.model.Address

object MockDataProvider {

    private val time = System.currentTimeMillis()
    val rating = 6.3f

    val weatherIntervals = listOf(
        WeatherInterval(WeatherType.SUNNY, time - 1_000_000, time - 600_000),
        WeatherInterval(WeatherType.RAIN, time - 600_000, time - 400_000),
        WeatherInterval(WeatherType.THUNDERSTORM, time - 400_000, time - 150_000),
        WeatherInterval(WeatherType.SUNNY, time - 150_000, time - 0),
    )
    val weatherData = listOf(
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
    val speeding = listOf(
        Speeding(time - 1_860_000L, 110.03f, Address(short = "ул. Вавилова, д. 46", full="", regionTypeFull="", regionTypeShort="")),
        Speeding(time - 810_000L, 90.7f, Address(short = "ул. Бутлерова, д. 4", full="", regionTypeFull="", regionTypeShort="")),
        Speeding(time - 510_000L, 81.9f, Address(short = "ул. Голубинская, д. 32/2", full="", regionTypeFull="", regionTypeShort=""))
    )
    val dangerousData = listOf(
        DangerousDriving(DangerousDrivingType.ACCELERATION, "ул. Большая Полянка, д. 1/3", time - 1_423_000L),
        DangerousDriving(DangerousDrivingType.SHARP_TURN, "ул. Паустовского, д. 8", time - 1_189_000L),
        DangerousDriving(DangerousDrivingType.SHARP_TURN, "ул. Голубинская, д. 32/2", time - 1_174_000L),
        DangerousDriving(DangerousDrivingType.ACCELERATION, "ул. Фитина, д. 2", time - 923_000L),
        DangerousDriving(DangerousDrivingType.ACCELERATION, "ул. Профсоюзная, д. 26", time - 423_000L),
    )

    val start = weatherData.minOf { it.timestamp }
    val divider = if (weatherData.last().timestamp - weatherData.first().timestamp > 1.5 * 3_600_000L)
        3_600_000
    else 60_000
}
