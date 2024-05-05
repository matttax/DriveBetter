package com.matttax.drivebetter.map.location

import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.permissions.ios.PermissionsController

actual object DefaultLocationTracker {

    actual fun get(): LocationTracker {
        return LocationTracker(
            PermissionsController()
        )
    }
}
