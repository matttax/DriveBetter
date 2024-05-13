package com.matttax.drivebetter.map.search

import com.matttax.drivebetter.map.domain.model.GeoPoint
import com.matttax.drivebetter.map.domain.model.SearchItem
import kotlinx.coroutines.flow.StateFlow

expect class SearchManagerFacade() {
    val searchResults: StateFlow<Result<List<SearchItem>>>
    fun submit(query: String, topLeftPoint: GeoPoint, bottomRightPoint: GeoPoint)
}
