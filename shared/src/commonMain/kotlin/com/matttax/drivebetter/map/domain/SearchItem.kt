package com.matttax.drivebetter.map.domain

data class SearchItem(
    val point: GeoPoint,
    val name: String?,
    val description: String?
)
