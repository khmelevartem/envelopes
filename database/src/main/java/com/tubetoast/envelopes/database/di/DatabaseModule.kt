package com.tubetoast.envelopes.database.di

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
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<StandardDatabase> { StandardDatabase.create(androidContext()) }
    single {
        EnvelopesRepositoryDatabaseBase(
            EnvelopeDataSource(
                get<StandardDatabase>().envelopeDao(),
                EnvelopeConverter()
            )
        )
    }
    single {
        CategoriesRepositoryDatabaseBase(
            CategoryDataSource(
                get<StandardDatabase>().categoryDao(),
                CategoryConverter()
            )
        )
    }
    single {
        SpendingRepositoryDatabaseBase(
            SpendingDataSource(
                get<StandardDatabase>().spendingDao(),
                SpendingConverter()
            )
        )
    }
}
