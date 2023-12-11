package com.tubetoast.envelopes.android.di

import com.tubetoast.envelopes.android.presentation.ui.screens.EditCategoryViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EditEnvelopeViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EnvelopesListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { EnvelopesListViewModel(get(), get()) }
    viewModel { EditEnvelopeViewModel(get()) }
    viewModel { EditCategoryViewModel(get(), get()) }

    // TODO refactor
    single<List<String>> {
        androidContext().assets.open("Monefy.csv").reader().use {
            it.readLines()
        }
    }
}
