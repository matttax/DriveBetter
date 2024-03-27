package com.matttax.drivebetter.history.presentation.components.ride

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.R
import com.matttax.drivebetter.history.presentation.components.ride.AddressTimeView
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
                painter = painterResource(
                    id = when (event) {
                        DangerousDrivingType.ACCELERATION -> R.drawable.ic_baseline_speed_24
                        DangerousDrivingType.SHARP_TURN -> R.drawable.ic_baseline_turn_24
                    }
                ),
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
