package com.tubetoast.envelopes.common.domain.snapshots

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Transaction

data class CategorySnapshot(
    val category: Category,
    val transactions: Set<Transaction<*>>
)
