package com.matttax.drivebetter.map.search

import com.matttax.drivebetter.map.domain.GeoPoint
import com.matttax.drivebetter.map.domain.SearchItem
import com.matttax.drivebetter.map.toGeoPoint
import com.matttax.drivebetter.map.toMapkitPoint
import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session
import com.yandex.runtime.Error
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.lighthousegames.logging.KmLog

actual class SearchManagerFacade actual constructor() {

    private val _searchResults = MutableStateFlow(Result.success(emptyList<SearchItem>()))
    actual val searchResults: StateFlow<Result<List<SearchItem>>>
        get() = _searchResults.asStateFlow()

    actual fun submit(query: String, topLeftPoint: GeoPoint, bottomRightPoint: GeoPoint) {
        log.d { "query submitted: $query" }
        searchManager.submit(
            query,
            Geometry.fromBoundingBox(
                BoundingBox(
                    topLeftPoint.toMapkitPoint(),
                    bottomRightPoint.toMapkitPoint()
                )
            ),
            searchOptions,
            searchSessionListener,
        )
    }

    private val searchManager = SearchFactory.getInstance().createSearchManager(
        SearchManagerType.COMBINED
    )

    private val searchOptions = SearchOptions().apply {
        searchTypes = SearchType.BIZ.value
        resultPageSize = 32
    }

    private val searchSessionListener = object : Session.SearchListener {
        override fun onSearchResponse(response: Response) {
            log.d { "search response: ${response.metadata.found} found" }
            _searchResults.update {
                Result.success(
                    response.collection.children
                        .mapNotNull { it.obj?.toSearchItem() }
                        .also {
                            log.d { "search response: $it" }
                        }
                )
            }
        }

        override fun onSearchError(error: Error) {
            log.e { "search error, ${error::class.simpleName}" }
            _searchResults.update {
                Result.failure(Exception(error::class.simpleName))
            }
        }
    }

    private fun GeoObject.toSearchItem(): SearchItem? {
        val point = geometry.firstOrNull()?.point?.toGeoPoint()
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
