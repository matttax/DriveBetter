package com.matttax.drivebetter.map.search

import com.matttax.drivebetter.map.domain.GeoPoint
import com.matttax.drivebetter.map.domain.SearchItem
import kotlinx.coroutines.flow.StateFlow

actual class SearchManagerFacade actual constructor() {
    actual val searchResults: StateFlow<Result<List<SearchItem>>>
        get() = TODO("Not yet implemented")

    actual fun submit(
        query: String,
        topLeftPoint: GeoPoint,
        bottomRightPoint: GeoPoint
    ) {
    }

}
