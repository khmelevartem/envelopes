package com.tubetoast.envelopes.common.di

import com.tubetoast.envelopes.common.data.CategoriesRepositoryImpl
import com.tubetoast.envelopes.common.data.EnvelopesRepositoryImpl
import com.tubetoast.envelopes.common.data.SpendingRepositoryImpl
import com.tubetoast.envelopes.common.domain.EditInteractor
import com.tubetoast.envelopes.common.domain.EnvelopesRepository
import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.common.domain.SnapshotsInteractorImpl
import com.tubetoast.envelopes.common.domain.stub.StubEditInteractorImpl
import org.koin.dsl.module

val domainModule = module {
    single<EnvelopesRepository> { EnvelopesRepositoryImpl() }
    single<SnapshotsInteractor> {
        SnapshotsInteractorImpl(
            SpendingRepositoryImpl(),
            CategoriesRepositoryImpl(),
            envelopesRepository = get()
        )
    }
    single<EditInteractor> { StubEditInteractorImpl(envelopesRepository = get()) }

}