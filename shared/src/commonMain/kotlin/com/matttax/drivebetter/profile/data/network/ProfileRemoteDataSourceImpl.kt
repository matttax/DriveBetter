package com.matttax.drivebetter.profile.data.network

import com.matttax.drivebetter.KtorService
import com.matttax.drivebetter.profile.data.ProfileRemoteDataSource
import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel
import com.matttax.drivebetter.util.provideDispatcher
import io.ktor.client.call.body
import io.ktor.client.request.post
import kotlinx.coroutines.withContext

class ProfileRemoteDataSourceImpl(
    private val ktorService: KtorService
) : ProfileRemoteDataSource {

    override suspend fun logIn(email: String, password: String): String? {
        return withContext(provideDispatcher().io) {
            ktorService.client.post("${KtorService.BASE_URL}/log_in").body<String?>().toString()
        }
    }

    override suspend fun signUp(profileDomainModel: ProfileDomainModel, password: String): String? {
        return withContext(provideDispatcher().io) {
            ktorService.client.post("${KtorService.BASE_URL}/signUp").body<String?>().toString()
        }
    }

}
