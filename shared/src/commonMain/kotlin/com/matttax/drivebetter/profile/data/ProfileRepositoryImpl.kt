package com.matttax.drivebetter.profile.data

import com.matttax.drivebetter.profile.data.mappers.toDatabaseEntity
import com.matttax.drivebetter.profile.data.token.LoginStorage
import com.matttax.drivebetter.profile.domain.ProfileRepository
import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel
import com.matttax.drivebetter.profile.domain.state.ProfileEvent
import com.matttax.drivebetter.profile.data.mappers.toDomainModel
import com.matttax.drivebetter.profile.data.network.TokenHolder
import org.lighthousegames.logging.KmLog

class ProfileRepositoryImpl(
    private val profileLocalDataSource: ProfileLocalDataSource,
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    private val loginStorage: LoginStorage
) : ProfileRepository {

    override suspend fun isLoggedIn(): ProfileDomainModel? {
        val token = loginStorage.token
        val profileId = loginStorage.profileId
        log.d { "saved profile id: $profileId, token: $token" }
        if (token == null || profileId == null) {
            loginStorage.clear()
            return null
        }
        return profileLocalDataSource.getById(profileId)?.toDomainModel()
    }

    override suspend fun logIn(request: ProfileEvent.EnterProfile): Boolean {
        val loginResult = profileRemoteDataSource.logIn(request.email, request.password)
        if (loginResult.isSuccess) {
            val token = loginResult.getOrNull()?.token
            loginStorage.token = token
            return token.isNullOrBlank().not()
        }
        return false
    }

    override suspend fun signUp(request: ProfileEvent.CreateProfile): Boolean {
        val registrationResult = profileRemoteDataSource.signUp(request.profile, request.password)
        log.d { registrationResult }
        if (registrationResult.isSuccess) {
            val token = registrationResult.getOrNull()?.token
            log.d { "token: $token" }
            loginStorage.token = token
            request.profile.toDatabaseEntity()?.let {
                profileLocalDataSource.addProfile(it)
                loginStorage.profileId = it.id
                log.d { "saving to db: $it" }
            } ?: {
                log.e { "unable to save to local database" }
            }
            return true
        }
        return false
    }

    override fun logOut() {
        loginStorage.clear()
    }

    companion object {
        private val log = KmLog("ProfileRepositoryImpl")
    }
}
