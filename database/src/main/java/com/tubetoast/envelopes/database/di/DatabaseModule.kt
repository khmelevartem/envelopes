package com.tubetoast.envelopes.database.di

import android.content.Context
import com.tubetoast.envelopes.common.domain.CategoryToGoalLinksRepository
import com.tubetoast.envelopes.database.data.CategoriesRepositoryDatabaseImpl
import com.tubetoast.envelopes.database.data.CategoryConverter
import com.tubetoast.envelopes.database.data.CategoryDataSource
import com.tubetoast.envelopes.database.data.CategoryToGoalLinksRepositoryDatabaseImpl
import com.tubetoast.envelopes.database.data.EnvelopeConverter
import com.tubetoast.envelopes.database.data.EnvelopeDataSource
import com.tubetoast.envelopes.database.data.EnvelopesRepositoryDatabaseImpl
import com.tubetoast.envelopes.database.data.GoalConverter
import com.tubetoast.envelopes.database.data.GoalsDataSource
import com.tubetoast.envelopes.database.data.GoalsRepositoryDatabaseImpl
import com.tubetoast.envelopes.database.data.SpendingConverter
import com.tubetoast.envelopes.database.data.SpendingDataSource
import com.tubetoast.envelopes.database.data.SpendingRepositoryDatabaseImpl
import com.tubetoast.envelopes.database.data.StandardDatabase
import org.koin.dsl.module

fun databaseModule(context: Context) = module {
    val envelopeConverter = EnvelopeConverter()
    val categoryConverter = CategoryConverter()
    val spendingConverter = SpendingConverter()
    val goalConverter = GoalConverter()
    val db = StandardDatabase.create(context)
    val envelopeDao = db.envelopeDao()
    val categoryDao = db.categoryDao()
    val spendingDao = db.spendingDao()
    val goalsDao = db.goalDao()
    val linksDao = db.linksDao()
    single {
        EnvelopesRepositoryDatabaseImpl(
            EnvelopeDataSource(envelopeDao, envelopeConverter)
        )
    }
    single {
        CategoriesRepositoryDatabaseImpl(
            CategoryDataSource(categoryDao, categoryConverter, envelopeDao, envelopeConverter)
        )
    }
    single {
        SpendingRepositoryDatabaseImpl(
            SpendingDataSource(spendingDao, spendingConverter, categoryDao, categoryConverter),
            get()
        )
    }
    single {
        GoalsRepositoryDatabaseImpl(
            GoalsDataSource(goalsDao, goalConverter),
            get()
        )
    }
    single<CategoryToGoalLinksRepository> {
        CategoryToGoalLinksRepositoryDatabaseImpl(
            linksDao,
            categoryDao,
            categoryConverter,
            goalsDao,
            goalConverter
        )
    }
}
