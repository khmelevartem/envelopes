package com.tubetoast.envelopes.database.di

import android.content.Context
import com.tubetoast.envelopes.common.domain.UpdatingCategoriesRepository
import com.tubetoast.envelopes.common.domain.UpdatingEnvelopesRepository
import com.tubetoast.envelopes.common.domain.UpdatingSpendingRepository
import com.tubetoast.envelopes.database.data.CategoriesRepositoryDatabaseBase
import com.tubetoast.envelopes.database.data.CategoryConverter
import com.tubetoast.envelopes.database.data.CategoryDataSource
import com.tubetoast.envelopes.database.data.EnvelopeConverter
import com.tubetoast.envelopes.database.data.EnvelopeDataSource
import com.tubetoast.envelopes.database.data.EnvelopesRepositoryDatabaseBase
import com.tubetoast.envelopes.database.data.SpendingConverter
import com.tubetoast.envelopes.database.data.SpendingDataSource
import com.tubetoast.envelopes.database.data.SpendingRepositoryDatabaseBase
import com.tubetoast.envelopes.database.data.StandardDatabase
import org.koin.dsl.module

fun databaseModule(context: Context) = module {
    val db = StandardDatabase.create(context)
    single<UpdatingEnvelopesRepository>() {
        EnvelopesRepositoryDatabaseBase(EnvelopeDataSource(db.envelopeDao(), EnvelopeConverter()))
    }
    single<UpdatingCategoriesRepository>() {
        CategoriesRepositoryDatabaseBase(CategoryDataSource(db.categoryDao(), CategoryConverter()))
    }
    single<UpdatingSpendingRepository>() {
        SpendingRepositoryDatabaseBase(SpendingDataSource(db.spendingDao(), SpendingConverter()))
    }
}