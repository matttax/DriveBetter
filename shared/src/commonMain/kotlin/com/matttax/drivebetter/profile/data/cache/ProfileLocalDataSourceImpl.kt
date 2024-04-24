package com.matttax.drivebetter.profile.data.cache

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.matttax.drivebetter.AccountDatabase
import com.matttax.drivebetter.ProfileEntity
import com.matttax.drivebetter.profile.data.ProfileLocalDataSource
import com.matttax.drivebetter.util.provideDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ProfileLocalDataSourceImpl(
    databaseDriverFactory: DatabaseDriverFactory
) : ProfileLocalDataSource {

    private val database = AccountDatabase(databaseDriverFactory.createDriver())
    override fun getAll(): Flow<List<ProfileEntity>> {
        return database.profileQueries
            .getProfiles()
            .asFlow()
            .mapToList(provideDispatcher().io)
    }

    override suspend fun getById(id: Long): ProfileEntity? {
        return withContext(provideDispatcher().io) {
            database.profileQueries.getById(id).executeAsOneOrNull()
        }
    }

    override suspend fun getLastId(): Long?{
        return withContext(provideDispatcher().io) {
            database.profileQueries.lastId().executeAsOneOrNull()
        }
    }

    override suspend fun addProfile(profileEntity: ProfileEntity) {
        withContext(provideDispatcher().io) {
            with(profileEntity) {
                database.profileQueries
                    .addProfile(id, name, city, gender, email, licenseId, dateOfBirth, dateOfIssue, rating, avatar)
            }
        }
    }

    override suspend fun uploadAvatarById(id: Long, avatarByteArray: ByteArray) {
        withContext(provideDispatcher().io) {
            database.profileQueries.updateAvatarById(avatarByteArray, id)
        }
    }

    override suspend fun deleteById(id: Long) {
        withContext(provideDispatcher().io) {
            database.profileQueries.deleteProfileById(id)
        }
    }
}
