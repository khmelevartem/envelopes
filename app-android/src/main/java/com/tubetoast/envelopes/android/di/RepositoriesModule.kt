package com.tubetoast.envelopes.android.di

import com.tubetoast.envelopes.common.data.CategoriesRepositoryInMemoryBase
import com.tubetoast.envelopes.common.data.CompositeCategoriesRepositoryBase
import com.tubetoast.envelopes.common.data.CompositeEnvelopesRepositoryBase
import com.tubetoast.envelopes.common.data.CompositeSpendingRepositoryBase
import com.tubetoast.envelopes.common.data.EnvelopesRepositoryWithUndefinedCategories
import com.tubetoast.envelopes.common.data.SpendingRepositoryInMemoryBase
import com.tubetoast.envelopes.common.di.CATEGORIES_REPO
import com.tubetoast.envelopes.common.di.ENVELOPES_REPO
import com.tubetoast.envelopes.common.di.SPENDING_REPO
import com.tubetoast.envelopes.common.di.api
import com.tubetoast.envelopes.common.domain.UpdatingCategoriesRepository
import com.tubetoast.envelopes.common.domain.UpdatingEnvelopesRepository
import com.tubetoast.envelopes.common.domain.UpdatingSpendingRepository
import com.tubetoast.envelopes.database.di.DatabaseApi
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoriesModule = module {
    single<UpdatingEnvelopesRepository>(named(ENVELOPES_REPO)) {
        CompositeEnvelopesRepositoryBase(
            api<DatabaseApi>().envelopesRepository,
            EnvelopesRepositoryWithUndefinedCategories()
        )
    }

    single<UpdatingCategoriesRepository>(named(CATEGORIES_REPO)) {
        CompositeCategoriesRepositoryBase(
            api<DatabaseApi>().categoriesRepository,
            CategoriesRepositoryInMemoryBase()
        )
    }

    single<UpdatingSpendingRepository>(named(SPENDING_REPO)) {
        CompositeSpendingRepositoryBase(
            api<DatabaseApi>().spendingRepository,
            SpendingRepositoryInMemoryBase()
        )
    }
}
