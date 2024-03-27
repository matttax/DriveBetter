package com.matttax.drivebetter.history.presentation.components.ride

import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import com.matttax.drivebetter.R
import com.matttax.drivebetter.history.presentation.model.WeatherType

@Composable
fun WeatherData(weatherType: WeatherType, modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        bitmap = ImageBitmap.imageResource(
            id = when(weatherType) {
                WeatherType.SUNNY -> R.drawable.ic_weather_clear_day
                WeatherType.FOG -> R.drawable.ic_weather_fog
                WeatherType.RAIN -> R.drawable.ic_weather_extreme_rain
                WeatherType.THUNDERSTORM -> R.drawable.ic_weather_thunderstorms_rain
            }
        ),
        contentDescription = null
    )
    Text(
        text = weatherType.message,
        style = MaterialTheme.typography.bodyMedium
    )
}
