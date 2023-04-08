package com.tubetoast.envelopes.common.domain.models

data class Spending(
    val amount: Amount,
    val date: Date,
    val comment: String? = null
) : ImmutableModel<Spending>()