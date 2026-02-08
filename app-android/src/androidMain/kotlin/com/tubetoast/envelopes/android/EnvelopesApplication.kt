package com.tubetoast.envelopes.android

import android.app.Application
import com.tubetoast.envelopes.android.di.androidModule
import com.tubetoast.envelopes.database.di.databaseAndroidModule
import com.tubetoast.envelopes.ui.di.applicationModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class EnvelopesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(*applicationModules() + androidModule + databaseAndroidModule)
        }
    }
}
