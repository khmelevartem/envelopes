package com.tubetoast.envelopes.android.presentation

import android.app.Application
import com.tubetoast.envelopes.android.di.applicationModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class EnvelopesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(*applicationModules)
        }
    }
}
