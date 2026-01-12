package com.tubetoast.envelopes.common.di

import com.tubetoast.envelopes.common.domain.AverageCalculator
import com.tubetoast.envelopes.common.domain.CategoryInteractor
import com.tubetoast.envelopes.common.domain.CategoryInteractorImpl
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.EnvelopeInteractorImpl
import com.tubetoast.envelopes.common.domain.GoalInteractor
import com.tubetoast.envelopes.common.domain.GoalInteractorImpl
import com.tubetoast.envelopes.common.domain.GoalSnapshotInteractor
import com.tubetoast.envelopes.common.domain.GoalSnapshotInteractorImpl
import com.tubetoast.envelopes.common.domain.InflationCalculator
import com.tubetoast.envelopes.common.domain.SelectedPeriodRepository
import com.tubetoast.envelopes.common.domain.SelectedPeriodRepositoryImpl
import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.common.domain.SnapshotsInteractorCachingImpl
import com.tubetoast.envelopes.common.domain.SpendingCalculator
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
            envelopesRepository = get(named(ENVELOPES_REPO))
        )
    }
    single<EnvelopeInteractor> { EnvelopeInteractorImpl(repository = get(named(ENVELOPES_REPO))) }
    single<CategoryInteractor> { CategoryInteractorImpl(repository = get(named(CATEGORIES_REPO))) }
    single<SpendingInteractor> { SpendingInteractorImpl(repository = get(named(SPENDING_REPO))) }
    single<SettingsRepository> { get<MutableSettingsRepository>() }
    single<GoalInteractor> { GoalInteractorImpl(get(named(GOALS_REPO))) }
    single<SelectedPeriodRepository> { SelectedPeriodRepositoryImpl(get()) }
    single { AverageCalculator(get()) }
    single { InflationCalculator(get()) }
    single { SpendingCalculator(get(), get()) }
    single<GoalSnapshotInteractor> {
        GoalSnapshotInteractorImpl(
            linksRepository = get(),
            spendingRepository = get(named(SPENDING_REPO))
        )
    }
}

const val SPENDING_REPO = "spending repo"
const val CATEGORIES_REPO = "categories repo"
const val ENVELOPES_REPO = "envelopes repo"
const val GOALS_REPO = "goals repo"
