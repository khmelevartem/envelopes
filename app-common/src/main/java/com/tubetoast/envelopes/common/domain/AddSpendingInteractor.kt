package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Transaction

class AddSpendingInteractor(
    private val repository: SpendingRepository
) {
    fun addSpending(spending: Transaction, category: Category) {

    }
}