package com.matttax.drivebetter.map

import cocoapods.YandexMapsMobile.YMKMapKit
import cocoapods.YandexMapsMobile.YMKMapView
import cocoapods.YandexMapsMobile.setApiKey
import cocoapods.YandexMapsMobile.sharedInstance
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import cocoapods.YandexMapsMobile.YMKGeoObjectCollectionItem
import cocoapods.YandexMapsMobile.YMKPoint
import cocoapods.YandexMapsMobile.YMKSearch
import cocoapods.YandexMapsMobile.YMKSearchOptions
import cocoapods.YandexMapsMobile.YMKSearchSearchManagerType
import cocoapods.YandexMapsMobile.YMKVisibleRegion
import cocoapods.YandexMapsMobile.YMKVisibleRegionUtils
import com.matttax.drivebetter.map.domain.GeoPoint
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun YandexMapView(locationFlow: StateFlow<GeoPoint?>, onCreate: () -> Unit) {
    val mapView = remember { YMKMapView() }
    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            YMKMapKit.setApiKey("46524574-c032-4d49-8c7c-5e7c8709543e")
            YMKMapKit.sharedInstance().onStart()
            val searchManager = YMKSearch.sharedInstance()?.createSearchManagerWithSearchManagerType(
                YMKSearchSearchManagerType.YMKSearchSearchManagerTypeCombined
            )
            searchManager?.submitWithText(
                "вкусно и точка",
                YMKVisibleRegionUtils.toPolygonWithVisibleRegion(YMKVisibleRegion()),
                YMKSearchOptions()
            ) { searchResponse, _ ->
                val points = searchResponse?.collection?.children?.mapNotNull {
                    ((it as YMKGeoObjectCollectionItem).obj?.geometry?.firstOrNull() as YMKPoint)
                }
                points?.forEach {
                    mapView.mapWindow?.map?.mapObjects?.addPlacemarkWithPoint(it)
                }
            }
            mapView
        }
    )
}
