package com.tubetoast.envelopes.android.di

import com.tubetoast.envelopes.android.presentation.ui.screens.ChooseEnvelopeViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EditCategoryViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EditEnvelopeViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EnvelopesListViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.SettingsViewModel
import com.tubetoast.envelopes.android.settings.MutableSettingsRepository
import com.tubetoast.envelopes.android.settings.SettingsRepository
import com.tubetoast.envelopes.android.settings.SettingsRepositoryDefaultImpl
import com.tubetoast.envelopes.android.settings.SettingsRepositorySharedPrefsImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { EnvelopesListViewModel(get(), get()) }
    viewModel { EditEnvelopeViewModel(get()) }
    viewModel { EditCategoryViewModel(get(), get(), get()) }
    viewModel { ChooseEnvelopeViewModel(get(), get()) }
    viewModel { SettingsViewModel(get()) }
    single<MutableSettingsRepository> {
        SettingsRepositorySharedPrefsImpl(
            androidContext(),
            SettingsRepositoryDefaultImpl()
        )
    }
    single<SettingsRepository> { get<MutableSettingsRepository>() }
}
