package com.tubetoast.envelopes.common.domain.snapshots

import com.tubetoast.envelopes.common.domain.models.Goal

data class GoalSnapshot(
    val goal: Goal,
    val categories: Set<CategorySnapshot>
)
