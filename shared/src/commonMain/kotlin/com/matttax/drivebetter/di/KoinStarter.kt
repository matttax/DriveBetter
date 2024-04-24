package com.matttax.drivebetter.di

import org.koin.core.context.startKoin

fun  initKoin() {
    startKoin {
        modules(getCommonModules())
    }
}
