package com.matttax.drivebetter.map.domain.model

data class SearchItem(
    val point: GeoPoint,
    val name: String?,
    val description: String?
)
