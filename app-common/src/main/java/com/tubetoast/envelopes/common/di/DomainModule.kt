package com.tubetoast.envelopes.common.di

import com.tubetoast.envelopes.common.data.CategoriesRepositoryImpl
import com.tubetoast.envelopes.common.data.EnvelopesRepositoryImpl
import com.tubetoast.envelopes.common.data.SpendingRepositoryImpl
import com.tubetoast.envelopes.common.domain.*
import org.koin.dsl.module

val domainModule = module {
    val envelopesRepository = EnvelopesRepositoryImpl()
    val categoriesRepository = CategoriesRepositoryImpl()
    single<SnapshotsInteractor> {
        SnapshotsInteractorImpl(
            SpendingRepositoryImpl(),
            categoriesRepository = categoriesRepository,
            envelopesRepository = envelopesRepository,
        )
    }
    single<EnvelopeInteractor> { EnvelopeInteractorImpl(repository = envelopesRepository, get()) }
    single<CategoryInteractor> { CategoryInteractorImpl(repository = categoriesRepository) }
}
