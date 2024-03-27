package com.matttax.drivebetter.history.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matttax.drivebetter.history.data.mock.MockDataProvider
import com.matttax.drivebetter.history.presentation.mappers.getPoints
import com.matttax.drivebetter.history.presentation.components.ride.*
import com.matttax.drivebetter.ui.common.StarRatingBar
import com.matttax.drivebetter.ui.common.Title
import com.matttax.drivebetter.ui.theme.TinkoffYellow
import com.matttax.drivebetter.ui.theme.tinkoffMedium
import com.matttax.drivebetter.ui.utils.DateUtils

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
                    text = DateUtils.timestampToDate(viewModel.startTimestamp)
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
        if (viewModel.getWeather().isNotEmpty()) {
            item {
                Text(
                    text = "Visibility",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            item {
                VisibilityLineChart(
                    pointsData = viewModel.getWeather().getPoints(),
                    initialTimestamp = viewModel.startTimestamp,
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
