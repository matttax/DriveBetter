package com.matttax.drivebetter

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.matttax.drivebetter.di.initKoin
import com.yandex.mapkit.MapKitFactory

class DriveBetterApp : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        MapKitFactory.setApiKey("46524574-c032-4d49-8c7c-5e7c8709543e")
        initKoin()
    }

    companion object ContextProvider {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }
}
