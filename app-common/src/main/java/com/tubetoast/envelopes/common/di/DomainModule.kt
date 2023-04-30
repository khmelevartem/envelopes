package com.tubetoast.envelopes.common.di

import com.tubetoast.envelopes.common.data.CategoriesRepositoryImpl
import com.tubetoast.envelopes.common.data.EnvelopesRepositoryImpl
import com.tubetoast.envelopes.common.data.SpendingRepositoryImpl
import com.tubetoast.envelopes.common.domain.EnvelopesInteractor
import com.tubetoast.envelopes.common.domain.EnvelopesInteractorImpl
import org.koin.dsl.module

val domainModule = module {
    single<EnvelopesInteractor> {
        EnvelopesInteractorImpl(
            SpendingRepositoryImpl(),
            CategoriesRepositoryImpl(),
            EnvelopesRepositoryImpl()
        )
    }

}