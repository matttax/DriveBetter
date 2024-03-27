package com.matttax.drivebetter.history.presentation.components.ride

import android.icu.text.DecimalFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.ui.common.KeyValueData

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
