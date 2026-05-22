package com.donai.app

import android.app.Application
import com.donai.app.core.di.sharedModule
import com.donai.app.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DonAIApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            printLogger() // 👈 CLAVE
            androidContext(this@DonAIApp)

            modules(
                sharedModule,
                viewModelModule,
                androidModule
            )
        }
    }
}