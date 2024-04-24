package com.matttax.drivebetter.map

import YandexMK.YMKMapKit
import YandexMK.YMKMapView
import YandexMK.setApiKey
import YandexMK.sharedInstance
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun YandexMapView() {
    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            YMKMapKit.setApiKey("46524574-c032-4d49-8c7c-5e7c8709543e")
            YMKMapKit.sharedInstance().onStart()
            YMKMapView()
        }
    )
}
