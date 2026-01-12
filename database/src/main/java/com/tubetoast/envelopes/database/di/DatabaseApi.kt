package com.tubetoast.envelopes.database.di

import com.tubetoast.envelopes.common.di.Api
import com.tubetoast.envelopes.common.domain.UpdatingCategoriesRepository
import com.tubetoast.envelopes.common.domain.UpdatingEnvelopesRepository
import com.tubetoast.envelopes.common.domain.UpdatingGoalsRepository
import com.tubetoast.envelopes.common.domain.UpdatingSpendingRepository
import com.tubetoast.envelopes.database.data.CategoriesRepositoryDatabaseImpl
import com.tubetoast.envelopes.database.data.EnvelopesRepositoryDatabaseImpl
import com.tubetoast.envelopes.database.data.GoalsRepositoryDatabaseImpl
import com.tubetoast.envelopes.database.data.SpendingRepositoryDatabaseImpl
import org.koin.java.KoinJavaComponent.inject

class DatabaseApi : Api {
    val categoriesRepository: UpdatingCategoriesRepository by inject(
        CategoriesRepositoryDatabaseImpl::class.java
    )
    val spendingRepository: UpdatingSpendingRepository by inject(
        SpendingRepositoryDatabaseImpl::class.java
    )
    val envelopesRepository: UpdatingEnvelopesRepository by inject(
        EnvelopesRepositoryDatabaseImpl::class.java
    )
    val goalsRepository: UpdatingGoalsRepository by inject(
        GoalsRepositoryDatabaseImpl::class.java
    )
}
