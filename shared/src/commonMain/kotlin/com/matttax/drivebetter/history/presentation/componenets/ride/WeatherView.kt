package com.matttax.drivebetter.history.presentation.componenets.ride

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.history.presentation.model.WeatherType
import com.matttax.drivebetter.ui.common.text.BodyText
import com.matttax.drivebetter.ui.utils.DateUtils
import com.matttax.drivebetter.ui.utils.DateUtils.timeAsString
import drivebetter.shared.generated.resources.Res
import drivebetter.shared.generated.resources.icon_weather_cloud
import drivebetter.shared.generated.resources.icons_weather_sunny
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun WeatherView(weatherType: WeatherType, startTimestamp: Long, endTimestamp: Long) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier.size(30.dp),
            painter = when(weatherType) {
                WeatherType.SUNNY -> painterResource(Res.drawable.icons_weather_sunny)
                else -> painterResource(Res.drawable.icon_weather_cloud)
            },
            contentDescription = null
        )
        BodyText(
            text = weatherType.message
        )
        BodyText(
            text = "${DateUtils.timestampToLocalDateTime(startTimestamp).timeAsString()} - ${DateUtils.timestampToLocalDateTime(endTimestamp).timeAsString()}",
            isImportant = false
        )
    }
}
