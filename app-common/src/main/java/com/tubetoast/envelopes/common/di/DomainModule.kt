package com.tubetoast.envelopes.common.di

import com.tubetoast.envelopes.common.data.SpendingRepositoryImpl
import com.tubetoast.envelopes.common.domain.AddSpendingInteractor
import com.tubetoast.envelopes.common.domain.AddSpendingInteractorImpl
import org.koin.dsl.module

val domainModule = module {
    single<AddSpendingInteractor> { AddSpendingInteractorImpl(SpendingRepositoryImpl()) }

}