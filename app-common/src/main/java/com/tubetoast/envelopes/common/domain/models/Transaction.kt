package com.tubetoast.envelopes.common.domain.models

data class Transaction(
    val amount: Amount,
    val date: Date,
    val comment: String? = null
)