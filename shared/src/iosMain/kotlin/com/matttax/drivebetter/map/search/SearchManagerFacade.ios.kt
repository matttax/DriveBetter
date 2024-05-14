package com.matttax.drivebetter.map.search

import cocoapods.YandexMapsMobile.YMKBoundingBox
import cocoapods.YandexMapsMobile.YMKGeoObject
import cocoapods.YandexMapsMobile.YMKGeoObjectCollectionItem
import cocoapods.YandexMapsMobile.YMKGeometry.Companion.geometryWithBoundingBox
import cocoapods.YandexMapsMobile.YMKPoint
import cocoapods.YandexMapsMobile.YMKSearch
import cocoapods.YandexMapsMobile.YMKSearchOptions
import cocoapods.YandexMapsMobile.YMKSearchSearchManagerType
import cocoapods.YandexMapsMobile.YMKSearchTypeBiz
import cocoapods.YandexMapsMobile.sharedInstance
import com.matttax.drivebetter.map.domain.model.GeoPoint
import com.matttax.drivebetter.map.domain.model.SearchItem
import com.matttax.drivebetter.map.toGeoPoint
import com.matttax.drivebetter.map.toMapkitPoint
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.lighthousegames.logging.KmLog
import platform.Foundation.NSNumber

@OptIn(ExperimentalForeignApi::class)
actual class SearchManagerFacade actual constructor() {

    private val _searchResults = MutableStateFlow(Result.success(emptyList<SearchItem>()))
    actual val searchResults: StateFlow<Result<List<SearchItem>>>
        get() = _searchResults

    private val searchManager = YMKSearch.sharedInstance()
        ?.createSearchManagerWithSearchManagerType(
            YMKSearchSearchManagerType.YMKSearchSearchManagerTypeCombined
        )

    private val searchOptions = YMKSearchOptions().apply {
        searchTypes = YMKSearchTypeBiz
        resultPageSize = NSNumber(64)
    }

    actual fun submit(
        query: String,
        topLeftPoint: GeoPoint,
        bottomRightPoint: GeoPoint
    ) {
        val northWest = topLeftPoint.toMapkitPoint()
        val southEast = bottomRightPoint.toMapkitPoint()
        searchManager?.submitWithText(
            query,
            geometryWithBoundingBox(
                YMKBoundingBox.boundingBoxWithSouthWest(
                    YMKPoint.pointWithLatitude(
                        southEast.latitude,
                        northWest.longitude
                    ),
                    YMKPoint.pointWithLatitude(
                        northWest.latitude,
                        southEast.longitude
                    )
                )
            ),
            searchOptions
        ) { searchResponse, error ->
            if (searchResponse != null) {
                _searchResults.update {
                    Result.success(
                        searchResponse.collection.children
                            .mapNotNull { (it as? YMKGeoObjectCollectionItem)?.obj?.toSearchItem() }
                            .also {
                                log.d { "search response: $it" }
                            }
                    )
                }
            } else {
                val message = error?.localizedFailureReason ?: "Unknown error"
                log.e { "search error, $message" }
                _searchResults.update {
                    Result.failure(RuntimeException(message))
                }
            }
        }
    }

    private fun YMKGeoObject.toSearchItem(): SearchItem? {
        val point = (geometry.firstOrNull() as? YMKPoint)?.toGeoPoint()
        return point?.let {
            SearchItem(
                it, name, descriptionText
            )
        }
    }

    companion object {
        private val log = KmLog("SearchManagerFacade")
    }
}
