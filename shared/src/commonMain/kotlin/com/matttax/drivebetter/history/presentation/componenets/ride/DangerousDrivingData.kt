package com.matttax.drivebetter.history.presentation.componenets.ride

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TurnSharpRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.history.presentation.model.DangerousDriving
import com.matttax.drivebetter.history.presentation.model.DangerousDrivingType

@Composable
fun DangerousDrivingData(dangerousDriving: DangerousDriving) {
    with(dangerousDriving) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                modifier = Modifier.weight(0.2f),
                imageVector = when (event) {
                    DangerousDrivingType.ACCELERATION -> Icons.Default.Speed
                    DangerousDrivingType.SHARP_TURN -> Icons.Default.TurnSharpRight
                },
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
