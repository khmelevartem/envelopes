package com.tubetoast.envelopes.common.domain.models

data class Spending(
    override val amount: Amount,
    override val date: Date,
    override val comment: String? = null
) : Transaction<Spending>()
