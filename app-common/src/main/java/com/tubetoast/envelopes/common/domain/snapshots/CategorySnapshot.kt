package com.tubetoast.envelopes.common.domain.snapshots

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Spending
import com.tubetoast.envelopes.common.domain.models.Transaction
import com.tubetoast.envelopes.common.domain.models.sum

data class CategorySnapshot(
    val category: Category,
    val transactions: Set<Transaction<*>>
) {
    fun isNotEmpty() = transactions.isNotEmpty()
}

fun CategorySnapshot.sum() = transactions
    .filterIsInstance<Spending>()
    .map { it.amount }
    .sum()
