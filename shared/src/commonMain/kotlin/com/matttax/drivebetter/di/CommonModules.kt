package com.matttax.drivebetter.di

import com.matttax.drivebetter.KtorService
import com.matttax.drivebetter.history.data.RideRepositoryImpl
import com.matttax.drivebetter.history.domain.RideRepository
import com.matttax.drivebetter.history.presentation.RidesHistoryViewModel
import com.matttax.drivebetter.map.data.DriveManagerImpl
import com.matttax.drivebetter.map.domain.DriveManager
import com.matttax.drivebetter.map.presentation.MapViewModel
import com.matttax.drivebetter.profile.data.ProfileRemoteDataSource
import com.matttax.drivebetter.profile.data.ProfileRepositoryImpl
import com.matttax.drivebetter.profile.data.network.ProfileRemoteDataSourceImpl
import com.matttax.drivebetter.profile.data.token.LoginStorage
import com.matttax.drivebetter.profile.data.token.LoginStorageImpl
import com.matttax.drivebetter.profile.domain.ProfileRepository
import com.matttax.drivebetter.profile.presentation.ProfileViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

private fun dataModule() = module {
    factory {
        ProfileViewModel(
            repository = get()
        )
    }

    factory {
        RidesHistoryViewModel(
            rideRepository = get(),
            loginStorage = get()
        )
    }

    factory {
        MapViewModel(
            driveManager = get()
        )
    }

    factory<RideRepository> {
        RideRepositoryImpl(
            ktorService = get(),
            loginStorage = get()
        )
    }

    factory<ProfileRepository> {
        ProfileRepositoryImpl(
            profileRemoteDataSource = get(),
            loginStorage = get(),
        )
    }

    single<DriveManager> {
        DriveManagerImpl(
            ktorService = get(),
            loginStorage = get()
        )
    }

    single<ProfileRemoteDataSource> {
        ProfileRemoteDataSourceImpl(
            ktorService = get()
        )
    }

    single<LoginStorage> {
        LoginStorageImpl()
    }

    single {
        KtorService()
    }
}

private fun commonModules() = listOf(
    dataModule()
)

fun getCommonModules(): List<Module> {
    return commonModules()
}
