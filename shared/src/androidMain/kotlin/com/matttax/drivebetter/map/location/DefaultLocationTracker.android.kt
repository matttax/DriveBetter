package com.matttax.drivebetter.map.location

import com.matttax.drivebetter.DriveBetterApp
import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.permissions.PermissionsController

actual object DefaultLocationTracker {
    actual fun get(): LocationTracker {
        return LocationTracker(
            PermissionsController(
                applicationContext = DriveBetterApp.context
            )
        )
    }
}
