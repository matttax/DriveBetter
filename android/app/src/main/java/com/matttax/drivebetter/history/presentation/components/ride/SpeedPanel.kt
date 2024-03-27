package com.matttax.drivebetter.history.presentation.components.ride

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.drive.presentation.componenets.SpeedView

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
