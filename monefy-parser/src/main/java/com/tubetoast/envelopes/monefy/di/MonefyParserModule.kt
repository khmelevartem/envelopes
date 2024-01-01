package com.tubetoast.envelopes.monefy.di

import com.tubetoast.envelopes.common.domain.CategoriesRepository
import com.tubetoast.envelopes.common.domain.CategoriesUpdatingRepository
import com.tubetoast.envelopes.common.domain.SpendingUpdatingRepository
import com.tubetoast.envelopes.monefy.data.MonefyCategoryRepositoryImpl
import com.tubetoast.envelopes.monefy.data.MonefySource
import com.tubetoast.envelopes.monefy.data.MonefySpendingRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val monefyParserModule = module {
    single { MonefySource(get(named("Monefy"))) }
    single<SpendingUpdatingRepository>(named("spending")) { MonefySpendingRepositoryImpl(get()) }
    single<CategoriesUpdatingRepository>(named("category")) { MonefyCategoryRepositoryImpl(get()) }
    single<CategoriesRepository>(named("category")) { get<CategoriesUpdatingRepository>((named("category"))) }
}
