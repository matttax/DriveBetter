package com.matttax.drivebetter.history.presentation.componenets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.history.presentation.RidesHistoryViewModel
import com.matttax.drivebetter.history.presentation.componenets.ride.DangerousDrivingData
import com.matttax.drivebetter.history.presentation.componenets.ride.LineChartSample
import com.matttax.drivebetter.history.presentation.componenets.ride.SpeedPanel
import com.matttax.drivebetter.history.presentation.componenets.ride.SpeedingView
import com.matttax.drivebetter.history.presentation.componenets.ride.TimePanel
import com.matttax.drivebetter.history.presentation.componenets.ride.WeatherView
import com.matttax.drivebetter.ui.common.StarRatingBar
import com.matttax.drivebetter.ui.common.text.BodyText
import com.matttax.drivebetter.ui.common.text.Title
import com.matttax.drivebetter.ui.utils.DateUtils
import com.matttax.drivebetter.ui.utils.DateUtils.asTwoStrings
import dev.icerock.moko.resources.compose.stringResource
import org.example.library.SharedRes

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RideScreen(
    viewModel: RidesHistoryViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(7.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Title(
                    text = DateUtils.timestampToLocalDateTime(viewModel.startTimestamp).asTwoStrings()
                )
                StarRatingBar(
                    rating = 7.5f,
                    starSize = 20.dp,
                    starCount = 10
                )
            }
        }
        item { Divider(Modifier.padding(vertical = 12.dp)) }
        stickyHeader {
            Title(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(SharedRes.strings.speed)
            )
        }
        item {
            SpeedPanel(
                maxSpeed = viewModel.currentRide?.maxSpeed?.toDouble() ?: 0.0,
                averageSpeed = viewModel.currentRide?.minSpeed?.toDouble() ?: 0.0
            )
        }
        item { Divider(Modifier.padding(vertical = 6.dp)) }
        stickyHeader {
            Title(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(SharedRes.strings.driving_time)
            )
        }
        item {
            TimePanel(
                lightNighttimePercentage = viewModel.currentRide?.lightNighttimePercentage ?: 0f,
                nighttimePercentage = viewModel.currentRide?.nighttimePercentage ?: 0f
            )
        }
        item { Divider(Modifier.padding(vertical = 6.dp)) }
        stickyHeader {
            Title(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(SharedRes.strings.weather)
            )
        }
        item {
            BodyText(
                text = stringResource(SharedRes.strings.conditions),
            )
        }
//        items(viewModel.getWeatherIntervals()) {
//            WeatherView(
//                weatherType = it.weatherType,
//                startTimestamp = it.startTimestamp,
//                endTimestamp = it.endTimestamp
//            )
//        }
        item {
            with(viewModel.getWeatherIntervals().last()) {
                WeatherView(
                    weatherType = weatherType,
                    startTimestamp = startTimestamp,
                    endTimestamp = endTimestamp
                )
            }
        }
        if (viewModel.getWeather().isNotEmpty()) {
            item {
//                VisibilityLineChart(
//                    pointsData = viewModel.getWeather().getPoints(),
//                    initialTimestamp = viewModel.startTimestamp,
//                    timeStep = viewModel.divider
//                )
                LineChartSample()
            }
        }
        item { Divider(Modifier.padding(vertical = 6.dp)) }
        stickyHeader {
            Title(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(SharedRes.strings.speeding)
            )
        }
        items(viewModel.currentRide?.speedingHistory ?: emptyList()) {
            SpeedingView(
                speed = it.speed,
                allowedSpeed = 80f,
                timestamp = it.timestamp,
                address = it.address.short
            )
        }
        item { Divider(Modifier.padding(vertical = 6.dp)) }
        stickyHeader {
            Title(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(SharedRes.strings.dangerous_driving)
            )
        }
        items(viewModel.getDangerousData()) {
            DangerousDrivingData(it)
        }
    }
}
