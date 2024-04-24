package com.matttax.drivebetter.profile.data

import com.matttax.drivebetter.ProfileEntity
import kotlinx.coroutines.flow.Flow

interface ProfileLocalDataSource {

    fun getAll(): Flow<List<ProfileEntity>>

    suspend fun getById(id: Long): ProfileEntity?

    suspend fun getLastId(): Long?

    suspend fun addProfile(profileEntity: ProfileEntity)

    suspend fun uploadAvatarById(id: Long, avatarByteArray: ByteArray)

    suspend fun deleteById(id: Long)
}
