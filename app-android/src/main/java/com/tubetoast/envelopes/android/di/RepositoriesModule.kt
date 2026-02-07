package com.tubetoast.envelopes.android.di

import com.tubetoast.envelopes.common.di.CATEGORIES_REPO
import com.tubetoast.envelopes.common.di.ENVELOPES_REPO
import com.tubetoast.envelopes.common.di.GOALS_REPO
import com.tubetoast.envelopes.common.di.SPENDING_REPO
import com.tubetoast.envelopes.common.di.api
import com.tubetoast.envelopes.common.domain.CategoriesRepository
import com.tubetoast.envelopes.common.domain.EnvelopesRepository
import com.tubetoast.envelopes.common.domain.GoalsRepository
import com.tubetoast.envelopes.common.domain.SpendingRepository
import com.tubetoast.envelopes.database.di.DatabaseApi
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoriesModule = module {
    single<EnvelopesRepository>(named(ENVELOPES_REPO)) {
        api<DatabaseApi>().envelopesRepository
    }

    single<CategoriesRepository>(named(CATEGORIES_REPO)) {
        api<DatabaseApi>().categoriesRepository
    }

    single<SpendingRepository>(named(SPENDING_REPO)) {
        api<DatabaseApi>().spendingRepository
    }

    single<GoalsRepository>(named(GOALS_REPO)) {
        api<DatabaseApi>().goalsRepository
    }
}
