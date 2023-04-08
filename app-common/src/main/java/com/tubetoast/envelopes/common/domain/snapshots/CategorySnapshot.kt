package com.tubetoast.envelopes.common.domain.snapshots

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Spending

data class CategorySnapshot(
    val category: Category,
    val spending: Set<Spending>,
)