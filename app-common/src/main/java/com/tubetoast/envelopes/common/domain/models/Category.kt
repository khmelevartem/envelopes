package com.tubetoast.envelopes.common.domain.models

class Category(
    val spendings: MutableList<Transaction>,
    val limit: Amount? = null
)