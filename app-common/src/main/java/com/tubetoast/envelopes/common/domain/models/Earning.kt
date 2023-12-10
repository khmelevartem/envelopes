package com.tubetoast.envelopes.common.domain.models

data class Earning(
    override val amount: Amount,
    override val date: Date,
    override val comment: String? = null
) : ImmutableModel<Earning>(), Operation
