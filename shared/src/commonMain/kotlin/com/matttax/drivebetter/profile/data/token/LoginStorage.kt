package com.matttax.drivebetter.profile.data.token

import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel

interface LoginStorage {

    var token: String?

    var lastProfile: ProfileDomainModel?

    var avatar: ByteArray?

    fun clear()
}
