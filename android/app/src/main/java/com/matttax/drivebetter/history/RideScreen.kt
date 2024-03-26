package com.matttax.drivebetter.history

import android.icu.text.DecimalFormat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.*
import com.matttax.drivebetter.R
import com.matttax.drivebetter.drive.presentation.componenets.SpeedView
import com.matttax.drivebetter.ui.common.StarRatingBar
import com.matttax.drivebetter.ui.common.Title
import com.matttax.drivebetter.ui.theme.TinkoffYellow
import com.matttax.drivebetter.ui.theme.tinkoffMedium
import okhttp3.internal.http.toHttpDateString
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RideScreen(viewModel: RideViewModel) {
    LazyColumn(
        modifier = Modifier.padding(7.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Title(
                    text = SimpleDateFormat("dd-MM-yyyy").format(viewModel.getStart())
                )
                StarRatingBar(
                    rating = MockDataProvider.rating,
                    starSize = 20.dp,
                    starCount = 10
                )
                Button(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    shape = RoundedCornerShape(15.dp),
                    onClick = { },
                    colors = ButtonDefaults.textButtonColors(TinkoffYellow)
                ) {
                    Text(
                        text = "Change role",
                        color = Color.Black,
                        style = TextStyle(
                            fontFamily = tinkoffMedium,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                        )
                    )
                }
            }
        }
        item { Divider(Modifier.padding(vertical = 12.dp)) }
        item { Title(text = "Speed") }
        item {
            SpeedPanel(
                maxSpeed = viewModel.currentRide?.maxSpeed?.toDouble() ?: 0.0,
                averageSpeed = viewModel.currentRide?.minSpeed?.toDouble() ?: 0.0
            )
        }
        item { Divider(Modifier.padding(vertical = 6.dp)) }
        item { Title(text = "Driving time") }
        item {
            TimePanel(
                lightNighttimePercentage = viewModel.currentRide?.lightNighttimePercentage ?: 0f,
                nighttimePercentage = viewModel.currentRide?.nighttimePercentage ?: 0f
            )
        }
        item { Divider(Modifier.padding(vertical = 6.dp)) }
        item { Title(text = "Weather") }
        item {
            Text(
                text = "Conditions",
                style = MaterialTheme.typography.titleMedium
            )
        }
        items(viewModel.getWeatherIntervals()) {
            WeatherView(
                weatherType = it.weatherType,
                startTimestamp = it.startTimestamp,
                endTimestamp = it.endTimestamp
            )
        }
        item {
            Text(
                text = "Visibility",
                style = MaterialTheme.typography.titleMedium
            )
        }
        item {
            if (viewModel.getWeather().isNotEmpty()) {
                LineChart(
                    pointsData = viewModel.getWeather().getPoints(),
                    initialTimestamp = viewModel.getStart(),
                    timeStep = viewModel.divider
                )
            }
        }
        item { Divider(Modifier.padding(vertical = 6.dp)) }
        item { Title(text = "Speeding") }
        items(viewModel.currentRide?.speedingHistory ?: emptyList()) {
            SpeedingView(
                speed = it.speed,
                allowedSpeed = 80f,
                timestamp = it.timestamp,
                address = it.address.short
            )
        }
        item { Divider(Modifier.padding(vertical = 6.dp)) }
        item { Title(text = "Dangerous driving") }
        items(viewModel.getDangerousData()) {
            DangerousDrivingData(it)
        }
    }
}

fun List<Weather>.getPoints(): List<Point> {
    if (isEmpty()) return emptyList()
    val timestamps = map { it.timestamp - MockDataProvider.start }
    val dataPoints = List(timestamps.size) {
        Point(
            x = timestamps[it].toFloat() / MockDataProvider.divider,
            y = MockDataProvider.mockWeatherData[it].visibility * 100,
            description = DateUtils.timestampToHourMinutes(MockDataProvider.mockWeatherData[it].timestamp)
        )
    }
    val firstPoint = listOf(Point(0f, 0f, ""))
    val lastPoint = listOf(Point(dataPoints.last().x + 1, 100f, ""))
    return firstPoint + dataPoints + lastPoint
}

data class DangerousDriving(
    val event: DangerousDrivingType,
    val address: String,
    val timestamp: Long
)

enum class DangerousDrivingType {
    ACCELERATION,
    SHARP_TURN
}

@Composable
fun DangerousDrivingData(dangerousDriving: DangerousDriving) {
    with(dangerousDriving) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp),
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                modifier = Modifier.weight(0.2f),
                painter = painterResource(
                    id = when (event) {
                        DangerousDrivingType.ACCELERATION -> R.drawable.ic_baseline_speed_24
                        DangerousDrivingType.SHARP_TURN -> R.drawable.ic_baseline_turn_24
                    }
                ),
                tint = Color.Blue,
                contentDescription = null
            )
            AddressTimeView(
                modifier = Modifier.weight(0.8f),
                address = address,
                timestamp = timestamp
            )
        }
    }
}

@Composable
fun AddressTimeView(address: String, timestamp: Long, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = DateUtils.timestampToHourMinutes(timestamp),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = address,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun SpeedingView(speed: Float, allowedSpeed: Float, timestamp: Long, address: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp),
        verticalAlignment = CenterVertically
    ) {
        Surface(
            modifier = Modifier.weight(0.15f),
            color = Color.Transparent,
            border = BorderStroke(4.dp, Color.Red),
            shape = CircleShape
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = speed.toInt().toString(),
                textAlign = TextAlign.Center
            )
        }
        AddressTimeView(
            modifier = Modifier.weight(0.7f),
            address = address,
            timestamp = timestamp
        )
        Surface(
            modifier = Modifier.weight(0.15f),
            color = Color.Transparent,
            border = BorderStroke(4.dp, Color.Green),
            shape = CircleShape
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = allowedSpeed.toInt().toString(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TimePanel(lightNighttimePercentage: Float, nighttimePercentage: Float) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp)
    ) {
        KeyValueData(
            key = "Daylight",
            value = "${DecimalFormat("#.##").format(100 - lightNighttimePercentage - nighttimePercentage)}%"
        )
        KeyValueData(
            key = "Light nighttime",
            value = "${DecimalFormat("#.##").format(lightNighttimePercentage)}%"
        )
        KeyValueData(
            key = "Nighttime",
            value = "${DecimalFormat("#.##").format(nighttimePercentage)}%"
        )
    }
}

@Composable
fun KeyValueData(key: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = key,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun SpeedPanel(maxSpeed: Double, averageSpeed: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Surface(
            color = Color.Transparent,
            border = BorderStroke(6.dp, Color.Red),
            shape = CircleShape
        ) {
            SpeedView(
                modifier = Modifier.padding(25.dp),
                speed = maxSpeed,
                title = "Max",
                size = 35
            )
        }
        Surface(
            color = Color.Transparent,
            border = BorderStroke(6.dp, MaterialTheme.colorScheme.primary),
            shape = CircleShape
        ) {
            SpeedView(
                modifier = Modifier.padding(25.dp),
                speed = averageSpeed,
                title = "Average",
                size = 35
            )
        }
    }
}

@Composable
fun WeatherView(weatherType: WeatherType, startTimestamp: Long, endTimestamp: Long) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        WeatherData(
            modifier = Modifier.size(40.dp),
            weatherType = weatherType
        )
        Text(
            text = DateUtils.timestampToHourMinutes(startTimestamp) + " - " + DateUtils.timestampToHourMinutes(endTimestamp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun WeatherData(weatherType: WeatherType, modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        bitmap = ImageBitmap.imageResource(
            id = when(weatherType) {
                WeatherType.SUNNY -> R.drawable.clear_day
                WeatherType.FOG -> R.drawable.fog
                WeatherType.RAIN -> R.drawable.extreme_rain
                WeatherType.THUNDERSTORM -> R.drawable.thunderstorms_rain
            }
        ),
        contentDescription = null
    )
    Text(
        text = weatherType.message,
        style = MaterialTheme.typography.bodyMedium
    )
}

enum class WeatherType(val message: String) {
    SUNNY("Sunny"),
    FOG("Fog"),
    RAIN("Rain"),
    THUNDERSTORM("Thunderstorm")
}

@Composable
fun LineChart(pointsData: List<Point>, initialTimestamp: Long, timeStep: Int) {
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