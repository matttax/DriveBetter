package com.matttax.drivebetter.profile.data.token

interface LoginStorage {
    var token: String?
    var profileId: Long?

    fun clear()
}
