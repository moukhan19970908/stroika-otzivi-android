package com.contractors.app

import android.app.Application
import android.content.Context
import com.contractors.app.domain.utils.YANDEX_TOKEN
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltAndroidApp
class App: Application() {
    @Inject
    @ApplicationContext
    lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(YANDEX_TOKEN)

    }
}