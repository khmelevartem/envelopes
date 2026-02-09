package com.tubetoast.envelopes.database.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.tubetoast.envelopes.common.di.CATEGORIES_REPO
import com.tubetoast.envelopes.common.di.ENVELOPES_REPO
import com.tubetoast.envelopes.common.di.GOALS_REPO
import com.tubetoast.envelopes.common.di.SPENDING_REPO
import com.tubetoast.envelopes.common.domain.CategoriesRepository
import com.tubetoast.envelopes.common.domain.CategoryToGoalLinksRepository
import com.tubetoast.envelopes.common.domain.EnvelopeSnapshotRepository
import com.tubetoast.envelopes.common.domain.EnvelopesRepository
import com.tubetoast.envelopes.common.domain.GoalSnapshotRepository
import com.tubetoast.envelopes.common.domain.GoalsRepository
import com.tubetoast.envelopes.common.domain.SpendingRepository
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun databaseModule(databaseBuilder: RoomDatabase.Builder<StandardDatabase>) =
    module {
        val envelopeConverter = EnvelopeConverter()
        val categoryConverter = CategoryConverter()
        val spendingConverter = SpendingConverter()
        val goalConverter = GoalConverter()

        val db = databaseBuilder
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()

        val envelopeDao = db.envelopeDao()
        val categoryDao = db.categoryDao()
        val spendingDao = db.spendingDao()
        val goalDao = db.goalDao()
        val categoryToGoalLinksDao = db.categoryToGoalLinksDao()
        val envelopeSnapshotDao = db.envelopeSnapshotDao()
        val goalSnapshotDao = db.goalSnapshotDao()

        single<EnvelopesRepository>(named(ENVELOPES_REPO)) {
            EnvelopesDatabaseRepository(
                EnvelopeDataSource(
                    envelopeDao,
                    envelopeConverter
                )
            )
        }
        single<CategoriesRepository>(named(CATEGORIES_REPO)) {
            CategoriesDatabaseRepository(
                CategoryDataSource(
                    categoryDao,
                    categoryConverter,
                    envelopeDao,
                    envelopeConverter
                )
            )
        }
        single<SpendingRepository>(named(SPENDING_REPO)) {
            SpendingDatabaseRepository(
                SpendingDataSource(
                    spendingDao,
                    spendingConverter,
                    categoryDao,
                    categoryConverter
                ),
                get()
            )
        }
        single<GoalsRepository>(named(GOALS_REPO)) {
            GoalsDatabaseRepository(
                GoalsDataSource(goalDao, goalConverter),
                get()
            )
        }

        single<CategoryToGoalLinksRepository> {
            CategoryToGoalLinksRepositoryDatabaseImpl(
                categoryToGoalLinksDao,
                categoryDao,
                categoryConverter,
                goalDao,
                goalConverter
            )
        }

        single<EnvelopeSnapshotRepository> {
            EnvelopeSnapshotDatabaseRepository(envelopeSnapshotDao, envelopeConverter, categoryConverter, spendingConverter)
        }

        single<GoalSnapshotRepository> {
            GoalSnapshotDatabaseRepository(goalSnapshotDao, goalConverter, categoryConverter, spendingConverter)
        }
    }
