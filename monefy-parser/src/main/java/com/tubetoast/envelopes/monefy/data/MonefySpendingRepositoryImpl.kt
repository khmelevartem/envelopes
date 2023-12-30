package com.tubetoast.envelopes.monefy.data

import com.tubetoast.envelopes.common.data.SpendingRepositoryBase
import com.tubetoast.envelopes.common.domain.models.Spending

class MonefySpendingRepositoryImpl(monefySource: MonefySource) : SpendingRepositoryBase() {
    init {
        monefySource.categorySnapshots.forEach { snapshot ->
            val category = snapshot.category
            val transactions = snapshot.transactions
            val spending = transactions.filterIsInstance<Spending>()
            sets[category.id] = spending.toMutableSet()
            spending.forEach { keys[it.id] = category.id }
        }
    }
}