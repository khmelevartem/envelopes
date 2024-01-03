package com.tubetoast.envelopes.monefy.di

import com.tubetoast.envelopes.common.di.CATEGORIES_REPO
import com.tubetoast.envelopes.common.di.SPENDING_REPO
import com.tubetoast.envelopes.common.domain.CategoriesRepository
import com.tubetoast.envelopes.common.domain.UpdatingCategoriesRepository
import com.tubetoast.envelopes.common.domain.UpdatingSpendingRepository
import com.tubetoast.envelopes.monefy.data.MonefyCategoryRepositoryImpl
import com.tubetoast.envelopes.monefy.data.MonefySource
import com.tubetoast.envelopes.monefy.data.MonefySpendingRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val monefyParserModule = module {
    single { MonefySource(get(named(MONEFY_INPUT))) }
    single<UpdatingSpendingRepository>(named(SPENDING_REPO)) {
        MonefySpendingRepositoryImpl(get())
    }
    single<UpdatingCategoriesRepository>(named(CATEGORIES_REPO)) {
        MonefyCategoryRepositoryImpl(get())
    }
    single<CategoriesRepository>(named(CATEGORIES_REPO)) {
        get<UpdatingCategoriesRepository>((named(CATEGORIES_REPO)))
    }
}

const val MONEFY_INPUT = "monefy input"
