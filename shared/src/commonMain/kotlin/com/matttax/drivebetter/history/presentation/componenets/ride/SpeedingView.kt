package com.matttax.drivebetter.history.presentation.componenets.ride

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.ui.common.text.SectionTitle

@Composable
fun SpeedingView(speed: Float, allowedSpeed: Float, timestamp: Long, address: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.weight(0.15f),
            color = Color.Transparent,
            border = BorderStroke(4.dp, Color.Red),
            shape = CircleShape
        ) {
            SectionTitle(
                modifier = Modifier.padding(10.dp),
                text = speed.toInt().toString(),
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
            SectionTitle(
                modifier = Modifier.padding(10.dp),
                text = allowedSpeed.toInt().toString(),
            )
        }
    }
}
