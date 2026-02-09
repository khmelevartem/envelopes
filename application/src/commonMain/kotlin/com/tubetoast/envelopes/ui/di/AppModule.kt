package com.tubetoast.envelopes.ui.di

import com.tubetoast.envelopes.common.settings.MutableSettingsRepository
import com.tubetoast.envelopes.common.settings.SettingsRepositoryDefaultImpl
import com.tubetoast.envelopes.ui.presentation.MainViewModel
import com.tubetoast.envelopes.ui.presentation.ui.screens.AverageViewViewModel
import com.tubetoast.envelopes.ui.presentation.ui.screens.EditCategoryViewModel
import com.tubetoast.envelopes.ui.presentation.ui.screens.EditEnvelopeViewModel
import com.tubetoast.envelopes.ui.presentation.ui.screens.EditGoalViewModel
import com.tubetoast.envelopes.ui.presentation.ui.screens.EnvelopesFilterViewModel
import com.tubetoast.envelopes.ui.presentation.ui.screens.EnvelopesListViewModel
import com.tubetoast.envelopes.ui.presentation.ui.screens.GoalsListViewModel
import com.tubetoast.envelopes.ui.presentation.ui.screens.InflationViewModel
import com.tubetoast.envelopes.ui.presentation.ui.screens.SelectEnvelopeViewModel
import com.tubetoast.envelopes.ui.presentation.ui.screens.SettingsViewModel
import com.tubetoast.envelopes.ui.presentation.ui.views.PeriodControlViewModel
import com.tubetoast.envelopes.ui.presentation.ui.views.SelectableCategoriesListViewModel
import com.tubetoast.envelopes.ui.settings.SettingsRepositoryKVStoreImpl
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    viewModel { EnvelopesListViewModel(get(), get(), get(), get()) }
    viewModel { EditEnvelopeViewModel(get(), get(), get()) }
    viewModel { EditCategoryViewModel(get(), get(), get()) }
    viewModel { SelectEnvelopeViewModel(get(), get(), get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { MainViewModel(get(), get(), get(named("main"))) }
    viewModel { PeriodControlViewModel(get(), get()) }
    viewModel { AverageViewViewModel(get(), get(), get()) }
    viewModel { InflationViewModel(get(), get(), get()) }
    viewModel { EnvelopesFilterViewModel(get()) }
    viewModel { GoalsListViewModel(get(), get(), get()) }
    viewModel { EditGoalViewModel(get(), get(), get()) }
    viewModel { SelectableCategoriesListViewModel(get(), get()) }
    single<MutableSettingsRepository> {
        SettingsRepositoryKVStoreImpl(
            get(named("settings")),
            SettingsRepositoryDefaultImpl()
        )
    }
}
