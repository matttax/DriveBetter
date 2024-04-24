package com.matttax.drivebetter.profile.domain

import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel
import com.matttax.drivebetter.profile.domain.state.ProfileEvent

interface ProfileRepository {

    suspend fun isLoggedIn(): ProfileDomainModel?

    suspend fun logIn(request: ProfileEvent.EnterProfile): Boolean

    suspend fun signUp(request: ProfileEvent.CreateProfile): Boolean
}
