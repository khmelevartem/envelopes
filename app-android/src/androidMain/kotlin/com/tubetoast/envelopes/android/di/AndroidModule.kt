package com.tubetoast.envelopes.android.di

import com.tubetoast.envelopes.android.SharedPrefsLocalKVStore
import com.tubetoast.envelopes.ui.presentation.LocalKVStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val androidModule = module {
    single<LocalKVStore>(named("main")) {
        SharedPrefsLocalKVStore(androidContext(), "EnvelopesMain")
    }

    single<LocalKVStore>(named("settings")) {
        SharedPrefsLocalKVStore(androidContext(), "EnvelopesSettings")
    }
}
