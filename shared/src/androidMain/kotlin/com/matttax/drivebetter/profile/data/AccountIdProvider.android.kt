package com.matttax.drivebetter.profile.data

import java.util.UUID

actual object AccountIdProvider {
    actual fun newId(): Long {
        val uuid = UUID.randomUUID()
        return uuid.mostSignificantBits + uuid.leastSignificantBits
    }
}
