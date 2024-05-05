package com.matttax.drivebetter.profile.data

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSUUID

actual object AccountIdProvider {
    @OptIn(ExperimentalForeignApi::class)
    actual fun newId(): Long {
        return NSUUID.UUID().hash.toLong()
    }
}
