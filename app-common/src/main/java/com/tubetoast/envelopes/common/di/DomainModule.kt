package com.tubetoast.envelopes.common.di

import com.tubetoast.envelopes.common.data.EnvelopesRepositoryWithUndefinedCategories
import com.tubetoast.envelopes.common.domain.CategoryInteractor
import com.tubetoast.envelopes.common.domain.CategoryInteractorImpl
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.EnvelopeInteractorImpl
import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.common.domain.SnapshotsInteractorImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val domainModule = module {
    val envelopesRepository = EnvelopesRepositoryWithUndefinedCategories()
    single<SnapshotsInteractor> {
        SnapshotsInteractorImpl(
            spendingRepository = get(named(SPENDING_REPO)),
            categoriesRepository = get(named(CATEGORIES_REPO)),
            envelopesRepository = envelopesRepository,
        )
    }
    single<EnvelopeInteractor> { EnvelopeInteractorImpl(repository = envelopesRepository) }
    single<CategoryInteractor> { CategoryInteractorImpl(repository = get(named(CATEGORIES_REPO))) }
}

const val SPENDING_REPO = "spending repo"
const val CATEGORIES_REPO = "categories repo"
const val ENVELOPES_REPO = "envelopes repo"