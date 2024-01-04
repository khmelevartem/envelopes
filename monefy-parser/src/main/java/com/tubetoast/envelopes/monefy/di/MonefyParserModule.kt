package com.tubetoast.envelopes.monefy.di

import com.tubetoast.envelopes.monefy.data.MonefyCategoryRepositoryImpl
import com.tubetoast.envelopes.monefy.data.MonefySource
import com.tubetoast.envelopes.monefy.data.MonefySpendingRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val monefyParserModule = module {
    single { MonefySource(get(named(MONEFY_INPUT))) }
    single { MonefySpendingRepositoryImpl(get()) }
    single { MonefyCategoryRepositoryImpl(get()) }
}

const val MONEFY_INPUT = "monefy input"
