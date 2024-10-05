package com.tubetoast.envelopes.android.di

import com.tubetoast.envelopes.android.presentation.MainViewModel
import com.tubetoast.envelopes.android.presentation.state.SelectedPeriodRepository
import com.tubetoast.envelopes.android.presentation.state.SelectedPeriodRepositoryImpl
import com.tubetoast.envelopes.android.presentation.ui.screens.ChooseEnvelopeViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EditCategoryViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EditEnvelopeViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EnvelopesListViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.SettingsViewModel
import com.tubetoast.envelopes.android.presentation.ui.views.TopAppBarViewModel
import com.tubetoast.envelopes.android.settings.SettingsRepositorySharedPrefsImpl
import com.tubetoast.envelopes.common.settings.MutableSettingsRepository
import com.tubetoast.envelopes.common.settings.SettingsRepository
import com.tubetoast.envelopes.common.settings.SettingsRepositoryDefaultImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { EnvelopesListViewModel(get(), get(), get(), get()) }
    viewModel { EditEnvelopeViewModel(get()) }
    viewModel { EditCategoryViewModel(get(), get(), get()) }
    viewModel { ChooseEnvelopeViewModel(get(), get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { MainViewModel(get(), get(), androidContext()) }
    viewModel { TopAppBarViewModel(get(), get()) }
    single<MutableSettingsRepository> {
        SettingsRepositorySharedPrefsImpl(
            androidContext(),
            SettingsRepositoryDefaultImpl()
        )
    }
    single<SettingsRepository> { get<MutableSettingsRepository>() }
    single<SelectedPeriodRepository> { SelectedPeriodRepositoryImpl(get()) }
}
