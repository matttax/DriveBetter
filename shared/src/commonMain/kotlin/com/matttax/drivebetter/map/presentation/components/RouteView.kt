package com.matttax.drivebetter.map.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.history.presentation.componenets.ride.KeyValueData
import com.matttax.drivebetter.map.routes.model.Route
import dev.icerock.moko.resources.compose.stringResource
import org.example.library.SharedRes

@Composable
fun RouteView(
    route: Route,
    onStartRide: () -> Unit
) {
    Column(
        modifier = Modifier.padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        KeyValueData(
            key = stringResource(SharedRes.strings.distance),
            value = route.metadata.distance
        )
        KeyValueData(
            key = stringResource(SharedRes.strings.time),
            value = route.metadata.time
        )
        KeyValueData(
            key = stringResource(SharedRes.strings.time_with_traffic),
            value = route.metadata.timeWithTraffic
        )
        KeyValueData(
            key = "Safety index",
            value = "8.16"
        )
        Button(
            modifier = Modifier.fillMaxWidth(0.8f),
            onClick = onStartRide,
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        vertical = 2.dp,
                        horizontal = 7.dp
                    ),
                text = stringResource(SharedRes.strings.start_ride),
                color = MaterialTheme.colors.surface
            )
        }
    }
}
