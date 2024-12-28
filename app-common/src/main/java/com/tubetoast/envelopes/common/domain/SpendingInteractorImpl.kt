package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Spending
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SpendingInteractorImpl(
    private val repository: UpdatingSpendingRepository
) : SpendingInteractor {
    override suspend fun getEarliestSpending(): Spending = withContext(Dispatchers.IO) {
        repository.getAll().associateBy { it.date }.let {
            it[it.keys.min()]!!
        }
    }
}
