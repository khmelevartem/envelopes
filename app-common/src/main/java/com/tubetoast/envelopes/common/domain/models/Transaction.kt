package com.tubetoast.envelopes.common.domain.models

abstract class Transaction: ImmutableModel {
    abstract val amount: Amount
    abstract val date: Date
    abstract val comment: String?
}
