package com.tubetoast.envelopes.database.di

import com.tubetoast.envelopes.common.di.Api
import com.tubetoast.envelopes.common.domain.CategoriesRepository
import com.tubetoast.envelopes.common.domain.EnvelopesRepository
import com.tubetoast.envelopes.common.domain.GoalsRepository
import com.tubetoast.envelopes.common.domain.SpendingRepository
import com.tubetoast.envelopes.database.data.CategoriesDatabaseRepository
import com.tubetoast.envelopes.database.data.EnvelopesDatabaseRepository
import com.tubetoast.envelopes.database.data.GoalsDatabaseRepository
import com.tubetoast.envelopes.database.data.SpendingDatabaseRepository
import org.koin.java.KoinJavaComponent.inject

class DatabaseApi : Api {
    val categoriesRepository: CategoriesRepository by inject(
        CategoriesDatabaseRepository::class.java
    )
    val spendingRepository: SpendingRepository by inject(
        SpendingDatabaseRepository::class.java
    )
    val envelopesRepository: EnvelopesRepository by inject(
        EnvelopesDatabaseRepository::class.java
    )
    val goalsRepository: GoalsRepository by inject(
        GoalsDatabaseRepository::class.java
    )
}
