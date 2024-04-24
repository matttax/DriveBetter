package com.matttax.drivebetter.profile.data

import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel

interface ProfileRemoteDataSource {

    suspend fun logIn(email: String, password: String): String?

    suspend fun signUp(profileDomainModel: ProfileDomainModel, password: String): String?
}
