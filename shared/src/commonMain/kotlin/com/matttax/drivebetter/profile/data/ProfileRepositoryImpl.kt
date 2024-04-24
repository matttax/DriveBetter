package com.matttax.drivebetter.profile.data

import com.matttax.drivebetter.profile.data.mappers.toDatabaseEntity
import com.matttax.drivebetter.profile.data.token.LoginStorage
import com.matttax.drivebetter.profile.domain.ProfileRepository
import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel
import com.matttax.drivebetter.profile.domain.state.ProfileEvent
import com.matttax.drivebetter.profile.data.mappers.toDomainModel

class ProfileRepositoryImpl(
    private val profileLocalDataSource: ProfileLocalDataSource,
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    private val loginStorage: LoginStorage
) : ProfileRepository {

    override suspend fun isLoggedIn(): ProfileDomainModel? {
        val token = loginStorage.token
        val profileId = loginStorage.profileId
        if (token == null || profileId == null) {
            loginStorage.clear()
            return null
        }
        return profileLocalDataSource.getById(profileId)?.toDomainModel()
    }

    override suspend fun logIn(request: ProfileEvent.EnterProfile): Boolean {
        val token = profileRemoteDataSource.logIn(request.email, request.password)
        loginStorage.token = token
        return token != null
    }

    override suspend fun signUp(request: ProfileEvent.CreateProfile): Boolean {
        val token = profileRemoteDataSource.signUp(request.profile, request.password)
        if (token != null) {
            loginStorage.token = token
            request.profile.toDatabaseEntity()?.let {
                profileLocalDataSource.addProfile(it)
            }
            return true
        }
        return false
    }
}
