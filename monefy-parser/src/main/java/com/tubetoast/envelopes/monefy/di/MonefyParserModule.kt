package com.tubetoast.envelopes.monefy.di

import com.tubetoast.envelopes.common.di.CATEGORIES_REPO
import com.tubetoast.envelopes.common.di.ENVELOPES_REPO
import com.tubetoast.envelopes.common.di.SPENDING_REPO
import com.tubetoast.envelopes.monefy.data.MonefyDataParser
import com.tubetoast.envelopes.monefy.data.MonefyDateParser
import com.tubetoast.envelopes.monefy.data.MonefyInteractor
import com.tubetoast.envelopes.monefy.data.MonefyTransactionParser
import org.koin.core.qualifier.named
import org.koin.dsl.module

val monefyParserModule = module {
    single {
        MonefyInteractor(
            monefyDataParser = MonefyDataParser(
                dateParser = MonefyDateParser(),
                operationParser = MonefyTransactionParser()
            ),
            envelopesRepository = get(named(ENVELOPES_REPO)),
            categoriesRepository = get(named(CATEGORIES_REPO)),
            spendingRepository = get(named(SPENDING_REPO)),
        )
    }
}
