package com.tubetoast.envelopes.common.di

import com.tubetoast.envelopes.common.data.EnvelopesRepositoryImpl
import com.tubetoast.envelopes.common.domain.EnvelopesInteractor
import com.tubetoast.envelopes.common.domain.EnvelopesInteractorImpl
import org.koin.dsl.module

val domainModule = module {
    single<EnvelopesInteractor> { EnvelopesInteractorImpl(EnvelopesRepositoryImpl()) }

}