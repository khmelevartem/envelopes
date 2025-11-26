package com.tubetoast.envelopes.common.domain.models

data class Goal(
    val target: Amount,
    val dateRange: DateRange,
    val name: String? = null
) : ImmutableModel<Goal>()
