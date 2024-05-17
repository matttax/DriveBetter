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
import com.matttax.drivebetter.voice.MessageDispatcher
import com.matttax.drivebetter.voice.MessageSpeller
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.dsl.onClose

@OptIn(DelicateCoroutinesApi::class)
private fun dataModule() = module {
    single {
        ProfileViewModel(
            repository = get()
        )
    }

    single {
        RidesHistoryViewModel(
            rideRepository = get(),
            loginStorage = get()
        )
    }

    single {
        MapViewModel(
            driveManager = get(),
            messageDispatcher = get()
        )
    }

    single<MessageDispatcher> {
        MessageDispatcher(
            messageSpeller = get()
        )
    }

    single<MessageSpeller> {
        MessageSpeller()
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
    } onClose {
        GlobalScope.launch {
            it?.sendBatch(isFinal = true)
        }
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
