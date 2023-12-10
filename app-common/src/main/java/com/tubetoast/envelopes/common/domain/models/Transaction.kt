package com.tubetoast.envelopes.common.domain.models

interface Transaction {
    val amount: Amount
    val date: Date
    val comment: String?
}
