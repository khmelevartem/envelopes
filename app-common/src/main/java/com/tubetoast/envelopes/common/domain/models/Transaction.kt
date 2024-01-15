package com.tubetoast.envelopes.common.domain.models

abstract class Transaction<T : Transaction<T>> : ImmutableModel<T>() {
    abstract val amount: Amount
    abstract val date: Date
    abstract val comment: String?
}
