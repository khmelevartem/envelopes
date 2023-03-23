package com.tubetoast.envelopes.common.domain.models

data class Envelope(
    val name: String,
    val categories: MutableList<Category>,
    val limit: Amount
) {
    val sum: Amount
        get() = categories.flatMap {
            it.spendings.map { transaction ->
                transaction.amount
            }
        }.sum()

    val percentage: Float
        get() = sum / limit
}