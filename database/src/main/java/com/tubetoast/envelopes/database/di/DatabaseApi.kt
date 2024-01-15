package com.tubetoast.envelopes.database.di

import com.tubetoast.envelopes.common.di.Api
import com.tubetoast.envelopes.common.domain.UpdatingCategoriesRepository
import com.tubetoast.envelopes.common.domain.UpdatingEnvelopesRepository
import com.tubetoast.envelopes.common.domain.UpdatingSpendingRepository
import com.tubetoast.envelopes.database.data.CategoriesRepositoryDatabaseBase
import com.tubetoast.envelopes.database.data.EnvelopesRepositoryDatabaseBase
import com.tubetoast.envelopes.database.data.SpendingRepositoryDatabaseBase
import org.koin.java.KoinJavaComponent.inject

class DatabaseApi : Api {
    val categoriesRepository: UpdatingCategoriesRepository by inject(
        CategoriesRepositoryDatabaseBase::class.java
    )
    val spendingRepository: UpdatingSpendingRepository by inject(
        SpendingRepositoryDatabaseBase::class.java
    )
    val envelopesRepository: UpdatingEnvelopesRepository by inject(
        EnvelopesRepositoryDatabaseBase::class.java
    )
}
