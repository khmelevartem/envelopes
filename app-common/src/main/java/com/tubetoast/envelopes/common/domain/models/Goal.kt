package com.tubetoast.envelopes.common.domain.models

data class Goal(
    val name: String,
    val target: Amount,
    val dateRange: DateRange
) : ImmutableModel<Goal>() {
    companion object {
        val EMPTY = Goal(name = "", target = Amount.ZERO, dateRange = DateRange.EMPTY)
    }
}
