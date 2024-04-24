package com.matttax.drivebetter.history.presentation.componenets.ride

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.ui.utils.NumericUtils.toPercentage
import dev.icerock.moko.resources.compose.stringResource
import org.example.library.SharedRes

@Composable
fun TimePanel(
    lightNighttimePercentage: Float,
    nighttimePercentage: Float
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp)
    ) {
        KeyValueData(
            key = stringResource(SharedRes.strings.driving_daylight_time),
            value = (100 - lightNighttimePercentage - nighttimePercentage).toPercentage()
        )
        KeyValueData(
            key = stringResource(SharedRes.strings.driving_light_nighttime_time),
            value = lightNighttimePercentage.toPercentage()
        )
        KeyValueData(
            key = stringResource(SharedRes.strings.driving_nighttime_time),
            value = nighttimePercentage.toPercentage()
        )
    }
}
