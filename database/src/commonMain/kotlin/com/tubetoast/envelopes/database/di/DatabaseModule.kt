package com.tubetoast.envelopes.database.di

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
import com.tubetoast.envelopes.database.data.DatabaseBuilder
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
import org.koin.core.qualifier.named
import org.koin.dsl.module

val databaseModule = module {

    val envelopeConverter = EnvelopeConverter()
    val categoryConverter = CategoryConverter()
    val spendingConverter = SpendingConverter()
    val goalConverter = GoalConverter()

    // 1. Database Instance
    single<StandardDatabase> {
        val builder: DatabaseBuilder = get()
        builder.initialize()
    }

    // 2. DAOs
    single { get<StandardDatabase>().envelopeDao() }
    single { get<StandardDatabase>().categoryDao() }
    single { get<StandardDatabase>().spendingDao() }
    single { get<StandardDatabase>().goalDao() }
    single { get<StandardDatabase>().linksDao() }
    single { get<StandardDatabase>().envelopeSnapshotDao() }
    single { get<StandardDatabase>().goalSnapshotDao() }

    single<EnvelopesRepository>(named(ENVELOPES_REPO)) {
        EnvelopesDatabaseRepository(
            EnvelopeDataSource(
                get(),
                envelopeConverter
            )
        )
    }
    single<CategoriesRepository>(named(CATEGORIES_REPO)) {
        CategoriesDatabaseRepository(
            CategoryDataSource(
                get(),
                categoryConverter,
                get(),
                envelopeConverter
            )
        )
    }
    single<SpendingRepository>(named(SPENDING_REPO)) {
        SpendingDatabaseRepository(
            SpendingDataSource(
                get(),
                spendingConverter,
                get(),
                categoryConverter
            ),
            get()
        )
    }
    single<GoalsRepository>(named(GOALS_REPO)) { GoalsDatabaseRepository(GoalsDataSource(get(), goalConverter), get()) }

    single<CategoryToGoalLinksRepository> {
        CategoryToGoalLinksRepositoryDatabaseImpl(get(), get(), categoryConverter, get(), goalConverter)
    }

    single<EnvelopeSnapshotRepository> {
        EnvelopeSnapshotDatabaseRepository(get(), envelopeConverter, categoryConverter, spendingConverter)
    }

    single<GoalSnapshotRepository> {
        GoalSnapshotDatabaseRepository(get(), goalConverter, categoryConverter, spendingConverter)
    }
}
