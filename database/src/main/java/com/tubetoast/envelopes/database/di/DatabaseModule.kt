package com.tubetoast.envelopes.database.di

import android.content.Context
import com.tubetoast.envelopes.common.domain.CategoryToGoalLinksRepository
import com.tubetoast.envelopes.common.domain.EnvelopeSnapshotRepository
import com.tubetoast.envelopes.common.domain.GoalSnapshotRepository
import com.tubetoast.envelopes.database.data.CategoriesDatabaseRepository
import com.tubetoast.envelopes.database.data.CategoryConverter
import com.tubetoast.envelopes.database.data.CategoryDataSource
import com.tubetoast.envelopes.database.data.CategoryToGoalLinksRepositoryDatabaseImpl
import com.tubetoast.envelopes.database.data.EnvelopeConverter
import com.tubetoast.envelopes.database.data.EnvelopeDataSource
import com.tubetoast.envelopes.database.data.EnvelopeSnapshotDatabaseRepository
import com.tubetoast.envelopes.database.data.EnvelopesDatabaseRepository
import com.tubetoast.envelopes.database.data.GoalConverter
import com.tubetoast.envelopes.database.data.GoalSnapshotDatabaseRepository
import com.tubetoast.envelopes.database.data.GoalsDataSource
import com.tubetoast.envelopes.database.data.GoalsDatabaseRepository
import com.tubetoast.envelopes.database.data.SpendingConverter
import com.tubetoast.envelopes.database.data.SpendingDataSource
import com.tubetoast.envelopes.database.data.SpendingDatabaseRepository
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
    val envelopeSnapshotDao = db.envelopeSnapshotDao()
    val goalSnapshotDao = db.goalSnapshotDao()
    single { EnvelopeConverter() }
    single { CategoryConverter() }
    single { SpendingConverter() }
    single { GoalConverter() }
    single {
        EnvelopesDatabaseRepository(
            EnvelopeDataSource(envelopeDao, envelopeConverter)
        )
    }
    single {
        CategoriesDatabaseRepository(
            CategoryDataSource(categoryDao, categoryConverter, envelopeDao, envelopeConverter)
        )
    }
    single {
        SpendingDatabaseRepository(
            SpendingDataSource(spendingDao, spendingConverter, categoryDao, categoryConverter),
            get()
        )
    }
    single {
        GoalsDatabaseRepository(
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
    single<EnvelopeSnapshotRepository> {
        EnvelopeSnapshotDatabaseRepository(
            envelopeSnapshotDao,
            envelopeConverter,
            categoryConverter,
            spendingConverter
        )
    }
    single<GoalSnapshotRepository> {
        GoalSnapshotDatabaseRepository(
            goalSnapshotDao,
            goalConverter,
            categoryConverter,
            spendingConverter
        )
    }
}
