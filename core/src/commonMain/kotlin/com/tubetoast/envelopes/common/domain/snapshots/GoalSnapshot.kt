package com.tubetoast.envelopes.common.domain.snapshots

import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.models.summarize

data class GoalSnapshot(
    val goal: Goal,
    val categories: Set<CategorySnapshot>
)

fun GoalSnapshot.sum(): Amount = categories.map { it.sum() }.summarize()

fun GoalSnapshot.percentage() = sum() / goal.target
