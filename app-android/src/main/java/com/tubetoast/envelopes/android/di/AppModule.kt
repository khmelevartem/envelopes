package com.tubetoast.envelopes.android.di

import com.tubetoast.envelopes.android.presentation.ui.screens.ChooseEnvelopeViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EditCategoryViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EditEnvelopeViewModel
import com.tubetoast.envelopes.android.presentation.ui.screens.EnvelopesListViewModel
import com.tubetoast.envelopes.monefy.di.MONEFY_INPUT
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.InputStream

val appModule = module {
    viewModel { EnvelopesListViewModel(get(), get()) }
    viewModel { EditEnvelopeViewModel(get()) }
    viewModel { EditCategoryViewModel(get(), get(), get()) }
    viewModel { ChooseEnvelopeViewModel(get(), get()) }

    single<InputStream>(named(MONEFY_INPUT)) {
        androidContext().assets.open("Monefy.csv")
    }
}
