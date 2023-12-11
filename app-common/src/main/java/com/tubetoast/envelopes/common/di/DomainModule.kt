package com.tubetoast.envelopes.common.di

import com.tubetoast.envelopes.common.data.CategoriesRepositoryImpl
import com.tubetoast.envelopes.common.data.EnvelopesRepositoryImpl
import com.tubetoast.envelopes.common.domain.CategoryInteractor
import com.tubetoast.envelopes.common.domain.CategoryInteractorImpl
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.EnvelopeInteractorImpl
import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.common.domain.SnapshotsInteractorImpl
import com.tubetoast.envelopes.common.domain.SpendingRepository
import org.koin.dsl.module

val domainModule = module {
    val envelopesRepository = EnvelopesRepositoryImpl()
    val categoriesRepository = CategoriesRepositoryImpl()
    single<SnapshotsInteractor> {
        SnapshotsInteractorImpl(
            get<SpendingRepository>(),
            categoriesRepository = categoriesRepository,
            envelopesRepository = envelopesRepository,
        )
    }
    single<EnvelopeInteractor> { EnvelopeInteractorImpl(repository = envelopesRepository, get()) }
    single<CategoryInteractor> { CategoryInteractorImpl(repository = categoriesRepository) }
}
