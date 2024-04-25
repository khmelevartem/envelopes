package com.tubetoast.envelopes.database.di

import android.content.Context
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
    val envelopeConverter = EnvelopeConverter()
    val categoryConverter = CategoryConverter()
    val spendingConverter = SpendingConverter()
    val db = StandardDatabase.create(context)
    val envelopeDao = db.envelopeDao()
    val categoryDao = db.categoryDao()
    val spendingDao = db.spendingDao()
    single {
        EnvelopesRepositoryDatabaseBase(
            EnvelopeDataSource(envelopeDao, envelopeConverter)
        )
    }
    single {
        CategoriesRepositoryDatabaseBase(
            CategoryDataSource(categoryDao, categoryConverter, envelopeDao, envelopeConverter)
        )
    }
    single {
        SpendingRepositoryDatabaseBase(
            SpendingDataSource(spendingDao, spendingConverter, categoryDao, categoryConverter)
        )
    }
}
