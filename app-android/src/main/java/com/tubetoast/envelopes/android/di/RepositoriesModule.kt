package com.tubetoast.envelopes.android.di

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
        api<DatabaseApi>().envelopesRepository
    }

    single<UpdatingCategoriesRepository>(named(CATEGORIES_REPO)) {
        api<DatabaseApi>().categoriesRepository
    }

    single<UpdatingSpendingRepository>(named(SPENDING_REPO)) {
        api<DatabaseApi>().spendingRepository
    }
}
