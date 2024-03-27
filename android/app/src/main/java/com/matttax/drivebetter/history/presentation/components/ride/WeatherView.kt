package com.matttax.drivebetter.history.presentation.components.ride

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.history.presentation.model.WeatherType
import com.matttax.drivebetter.ui.utils.DateUtils

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
            text = DateUtils.timestampToHourMinutes(startTimestamp) + " - " + DateUtils.timestampToHourMinutes(
                endTimestamp
            ),
            style = MaterialTheme.typography.bodySmall
        )
    }
}
