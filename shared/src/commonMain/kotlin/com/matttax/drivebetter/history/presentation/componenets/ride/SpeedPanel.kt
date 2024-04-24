package com.matttax.drivebetter.history.presentation.componenets.ride

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.ui.theme.Red
import com.matttax.drivebetter.ui.theme.Yellow
import dev.icerock.moko.resources.compose.stringResource
import org.example.library.SharedRes

@Composable
fun SpeedPanel(
    maxSpeed: Double,
    averageSpeed: Double
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Surface(
            color = Color.Transparent,
            border = BorderStroke(
                width = 6.dp,
                color = Red
            ),
            shape = CircleShape
        ) {
            SpeedView(
                modifier = Modifier.padding(40.dp),
                speed = maxSpeed,
                title = stringResource(SharedRes.strings.max_speed)
            )
        }
        Surface(
            color = Color.Transparent,
            border = BorderStroke(
                width = 6.dp,
                color = Yellow
            ),
            shape = CircleShape
        ) {
            SpeedView(
                modifier = Modifier.padding(40.dp),
                speed = averageSpeed,
                title = stringResource(SharedRes.strings.average_speed)
            )
        }
    }
}
