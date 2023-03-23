package com.tubetoast.envelopes.common.di

import com.tubetoast.envelopes.common.domain.AddSpendingInteractor
import org.koin.java.KoinJavaComponent.inject

class DomainApi : Api {
    val addSpendingInteractor: AddSpendingInteractor by inject(AddSpendingInteractor::class.java)
}