package com.tubetoast.envelopes.monefy.data

import com.tubetoast.envelopes.common.data.SpendingRepositoryImpl
import com.tubetoast.envelopes.common.domain.models.Spending

class MonefySpendingRepositoryImpl(monefySource: MonefySource) : SpendingRepositoryImpl() {
    init {
        monefySource.categorySnapshots.forEach { snapshot ->
            val category = snapshot.category
            val transactions = snapshot.transactions
            val spending = transactions.filterIsInstance<Spending>()
            sets[category.hash] = spending.toMutableSet()
            spending.forEach { keys[it.hash] = category.hash }
        }
    }
}