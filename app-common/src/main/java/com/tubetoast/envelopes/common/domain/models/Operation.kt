package com.tubetoast.envelopes.common.domain.models

interface Operation {
    val amount: Amount
    val date: Date
    val comment: String?
}
