package com.tubetoast.envelopes.monefy.di

import com.tubetoast.envelopes.common.domain.CategoriesRepository
import com.tubetoast.envelopes.common.domain.SpendingRepository
import com.tubetoast.envelopes.monefy.data.MonefySource
import com.tubetoast.envelopes.monefy.data.MonefyCategoryRepositoryImpl
import com.tubetoast.envelopes.monefy.data.MonefySpendingRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val monefyParserModule = module {
    single { MonefySource(get(named("Monefy"))) }
    single<SpendingRepository> { MonefySpendingRepositoryImpl(get()) }
    single<CategoriesRepository> { MonefyCategoryRepositoryImpl(get()) }
}
