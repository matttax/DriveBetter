package com.matttax.drivebetter.history.presentation.components.ride

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.*
import com.matttax.drivebetter.ui.utils.DateUtils

@Composable
fun VisibilityLineChart(pointsData: List<Point>, initialTimestamp: Long, timeStep: Int) {
    val steps = 5
    val xAxisData = AxisData.Builder()
        .axisStepSize(80.dp)
        .topPadding(105.dp)
        .steps(pointsData.maxOf { it.x }.toInt() + 5)
        .labelData { if (it > 0) DateUtils.timestampToHourMinutes(initialTimestamp + timeStep * it) else "" }
        .labelAndAxisLinePadding(15.dp)
        .build()
    val yAxisData = AxisData.Builder()
        .steps(steps)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            val yMin = pointsData.minOf { it.y }
            val yMax = pointsData.maxOf { it.y }
            val yScale = (yMax - yMin) / steps
            ((i * yScale) + yMin).formatToSinglePrecision()
        }.build()
    val data = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    lineStyle = LineStyle(
                        color = Color.Green,
                    ),
                    intersectionPoint = IntersectionPoint(
                        radius = 3.dp
                    ),
                    selectionHighlightPoint = null,
                    shadowUnderLine = ShadowUnderLine(
                        color = Color.Green
                    ),
                    selectionHighlightPopUp = SelectionHighlightPopUp(
                        popUpLabel = { x, y ->
                            DateUtils.timestampToHourMinutes(initialTimestamp + (x * timeStep).toLong()) +
                                    ", $y%"
                        }
                    )
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines()
    )
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        lineChartData = data
    )
}
