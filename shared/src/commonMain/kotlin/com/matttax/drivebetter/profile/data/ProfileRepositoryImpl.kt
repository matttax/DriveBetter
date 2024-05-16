package com.matttax.drivebetter.profile.data

import com.matttax.drivebetter.profile.data.token.LoginStorage
import com.matttax.drivebetter.profile.domain.ProfileRepository
import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel
import com.matttax.drivebetter.profile.domain.state.ProfileEvent
import org.lighthousegames.logging.KmLog

class ProfileRepositoryImpl(
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    private val loginStorage: LoginStorage
) : ProfileRepository {

    override suspend fun isLoggedIn(): ProfileDomainModel? {
        val token = loginStorage.token
        val lastProfile = loginStorage.lastProfile
        log.d { "saved profile id: ${lastProfile?.uuid}, token: $token" }
        if (token == null || lastProfile == null) {
            loginStorage.clear()
            return null
        }
        return lastProfile
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
            request.profile.let {
                loginStorage.lastProfile = it
                log.d { "saving to db: $it" }
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
