package com.tubetoast.envelopes.common.di

import com.tubetoast.envelopes.common.domain.AverageCalculator
import com.tubetoast.envelopes.common.domain.CategoryInteractor
import com.tubetoast.envelopes.common.domain.CategoryInteractorImpl
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.EnvelopeInteractorImpl
import com.tubetoast.envelopes.common.domain.InflationCalculator
import com.tubetoast.envelopes.common.domain.SelectedPeriodRepository
import com.tubetoast.envelopes.common.domain.SelectedPeriodRepositoryImpl
import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.common.domain.SnapshotsInteractorCachingImpl
import com.tubetoast.envelopes.common.domain.SpendingInteractor
import com.tubetoast.envelopes.common.domain.SpendingInteractorImpl
import com.tubetoast.envelopes.common.settings.MutableSettingsRepository
import com.tubetoast.envelopes.common.settings.SettingsRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val domainModule = module {
    single<SnapshotsInteractor> {
        SnapshotsInteractorCachingImpl(
            spendingRepository = get(named(SPENDING_REPO)),
            categoriesRepository = get(named(CATEGORIES_REPO)),
            envelopesRepository = get(named(ENVELOPES_REPO)),
            selectedPeriodRepository = get()
        )
    }
    single<EnvelopeInteractor> { EnvelopeInteractorImpl(repository = get(named(ENVELOPES_REPO))) }
    single<CategoryInteractor> { CategoryInteractorImpl(repository = get(named(CATEGORIES_REPO))) }
    single<SpendingInteractor> { SpendingInteractorImpl(repository = get(named(SPENDING_REPO))) }
    single<SettingsRepository> { get<MutableSettingsRepository>() }
    single<SelectedPeriodRepository> { SelectedPeriodRepositoryImpl(get()) }
    single { AverageCalculator(get()) }
    single { InflationCalculator(get()) }
}

const val SPENDING_REPO = "spending repo"
const val CATEGORIES_REPO = "categories repo"
const val ENVELOPES_REPO = "envelopes repo"
