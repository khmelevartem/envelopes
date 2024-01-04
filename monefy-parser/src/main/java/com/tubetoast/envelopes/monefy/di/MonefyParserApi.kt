package com.tubetoast.envelopes.monefy.di

import com.tubetoast.envelopes.common.di.Api
import com.tubetoast.envelopes.common.domain.UpdatingCategoriesRepository
import com.tubetoast.envelopes.common.domain.UpdatingSpendingRepository
import com.tubetoast.envelopes.monefy.data.MonefyCategoryRepositoryImpl
import com.tubetoast.envelopes.monefy.data.MonefySpendingRepositoryImpl
import org.koin.java.KoinJavaComponent.inject

class MonefyParserApi : Api {
    val categoriesRepository: UpdatingCategoriesRepository by inject(MonefyCategoryRepositoryImpl::class.java)
    val spendingRepository: UpdatingSpendingRepository by inject(MonefySpendingRepositoryImpl::class.java)
}
