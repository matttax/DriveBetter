package com.matttax.drivebetter.profile.data

import com.matttax.drivebetter.profile.data.network.model.TokenHolder
import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel

interface ProfileRemoteDataSource {

    suspend fun logIn(email: String, password: String): Result<ProfileDomainModel>

    suspend fun signUp(profileDomainModel: ProfileDomainModel, password: String): Result<TokenHolder>

    suspend fun edit(newProfile: ProfileDomainModel, token: String? = null): Result<ProfileDomainModel>
}
