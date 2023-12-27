package com.tubetoast.envelopes.common.di

import com.tubetoast.envelopes.common.data.EnvelopesRepositoryImpl
import com.tubetoast.envelopes.common.domain.CategoryInteractor
import com.tubetoast.envelopes.common.domain.CategoryInteractorImpl
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.EnvelopeInteractorImpl
import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.common.domain.SnapshotsInteractorImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val domainModule = module {
    val envelopesRepository = EnvelopesRepositoryImpl()
    single<SnapshotsInteractor> {
        SnapshotsInteractorImpl(
            spendingRepository = get(named("spending")),
            categoriesRepository = get(named("category")),
            envelopesRepository = envelopesRepository,
        )
    }
    single<EnvelopeInteractor> { EnvelopeInteractorImpl(repository = envelopesRepository) }
    single<CategoryInteractor> { CategoryInteractorImpl(repository = get(named("category"))) }
}
