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
        return lastProfile.copy(avatarByteArray = loginStorage.avatar)
    }

    override suspend fun logIn(request: ProfileEvent.EnterProfile): ProfileDomainModel? {
        return try {
            val loginResult = profileRemoteDataSource.logIn(request.email, request.password)
            log.d { "login result: $loginResult" }
            if (loginResult.isSuccess) {
                val profile = loginResult.getOrNull()
                loginStorage.token = profile?.token
                loginStorage.lastProfile = profile
                profile
            } else null
        } catch (ex: Throwable) {
            null
        }
    }

    override suspend fun signUp(request: ProfileEvent.CreateProfile): Boolean {
        return try {
            val registrationResult = profileRemoteDataSource.signUp(request.profile, request.password)
            log.d { "registration result $registrationResult" }
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
            false
        } catch (ex: Throwable) { false }
    }

    override suspend fun edit(newData: ProfileDomainModel): ProfileDomainModel? {
        val editResult = profileRemoteDataSource.edit(newData, loginStorage.token)
        log.d { "edit result: $editResult" }
        if (editResult.isSuccess) {
            editResult.getOrNull()?.let {
                loginStorage.lastProfile = it
            }
        }
        return editResult.getOrNull()
    }

    override suspend fun changeAvatar(avatar: ByteArray): Boolean {
        return false
    }

    override fun logOut() {
        loginStorage.clear()
    }

    companion object {
        private val log = KmLog("ProfileRepositoryImpl")
    }
}
