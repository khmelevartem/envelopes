package com.tubetoast.envelopes.database.di

import com.tubetoast.envelopes.database.data.provideAndroidBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseAndroidModule = module {
    single { provideAndroidBuilder(androidContext()) }
    includes(databaseModule)
}
