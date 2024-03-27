package com.matttax.drivebetter.history.presentation.components.ride

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.matttax.drivebetter.ui.utils.DateUtils

@Composable
fun AddressTimeView(address: String, timestamp: Long, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = DateUtils.timestampToHourMinutes(timestamp),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = address,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
