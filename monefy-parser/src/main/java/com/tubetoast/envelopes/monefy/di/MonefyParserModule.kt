package com.tubetoast.envelopes.monefy.di

import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.monefy.data.SnapshotsInteractorMonefyImpl
import org.koin.dsl.module

val monefyParserModule = module {
// TODO refactor
    single<SnapshotsInteractor> { SnapshotsInteractorMonefyImpl(get()) }
}
