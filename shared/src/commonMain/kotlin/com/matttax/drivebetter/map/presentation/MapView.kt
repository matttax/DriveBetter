package com.matttax.drivebetter.map.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.matttax.drivebetter.map.YandexMapView
import com.matttax.drivebetter.ui.common.text.BodyText
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import dev.icerock.moko.resources.compose.fontFamilyResource
import moe.tlaster.precompose.viewmodel.viewModel
import org.example.library.SharedRes

@Composable
fun MapView(
    mapViewModel: MapViewModel
) {
    BindLocationTrackerEffect(mapViewModel.locationTracker)
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = "",
            onValueChange = {},
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface,
            ),
            placeholder = {
                BodyText(
                    text = "Destination",
                    isImportant = false
                )
            },
            textStyle = TextStyle(
                fontFamily = fontFamilyResource(SharedRes.fonts.yandex_sans_display_light)
            )
        )
        YandexMapView(mapViewModel.currentLocation) {
            mapViewModel.initSearch()
        }
    }
}
