package com.tubetoast.envelopes.common.di

import com.tubetoast.envelopes.common.domain.Interactor
import org.koin.dsl.module

val domainModule = module {
    single<Interactor> { Interactor() }
}