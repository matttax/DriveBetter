package com.matttax.drivebetter.history.presentation.mappers

import co.yml.charts.common.model.Point
import com.matttax.drivebetter.history.data.mock.MockDataProvider
import com.matttax.drivebetter.history.domain.Weather
import com.matttax.drivebetter.ui.utils.DateUtils

fun List<Weather>.getPoints(): List<Point> {
    if (isEmpty()) return emptyList()
    val timestamps = map { it.timestamp - MockDataProvider.start }
    val dataPoints = List(timestamps.size) {
        Point(
            x = timestamps[it].toFloat() / MockDataProvider.divider,
            y = get(it).visibility * 100,
            description = DateUtils.timestampToHourMinutes(get(it).timestamp)
        )
    }
    val firstPoint = listOf(Point(0f, 0f, ""))
    val lastPoint = listOf(Point(dataPoints.last().x + 1, 100f, ""))
    return firstPoint + dataPoints + lastPoint
}
