package com.matttax.drivebetter.map.location

import dev.icerock.moko.geo.LocationTracker

expect object DefaultLocationTracker {
    fun get(): LocationTracker
}
