package com.matttax.drivebetter.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import cocoapods.YandexMapsMobile.sharedInstance
import cocoapods.YandexMapsMobile.YMKGeoObjectCollectionItem
import cocoapods.YandexMapsMobile.YMKMapView
import cocoapods.YandexMapsMobile.YMKPoint
import cocoapods.YandexMapsMobile.YMKSearch
import cocoapods.YandexMapsMobile.YMKSearchOptions
import cocoapods.YandexMapsMobile.YMKSearchSearchManagerType
import cocoapods.YandexMapsMobile.YMKVisibleRegion
import cocoapods.YandexMapsMobile.YMKVisibleRegionUtils
import com.matttax.drivebetter.map.domain.model.RidePoint
import com.matttax.drivebetter.map.domain.model.SearchItem
import com.matttax.drivebetter.map.presentation.states.MapViewState
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun YandexMapView(
    locationFlow: StateFlow<RidePoint?>,
    selectedItemFlow: StateFlow<SearchItem?>,
    selectedPolyline: StateFlow<Any?>,
    searchResultsFlow: Flow<List<SearchItem>>,
    onCreate: () -> Unit,
    onUpdate: (MapViewState) -> Unit,
    onDestinationSelected: (SearchItem) -> Unit
) {
    val mapView = remember { YMKMapView() }
    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = {
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
