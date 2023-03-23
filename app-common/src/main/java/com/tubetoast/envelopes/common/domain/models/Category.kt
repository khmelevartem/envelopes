package com.tubetoast.envelopes.common.domain.models

data class Category(
    val name: String,
    val spending: List<Transaction>,
    val limit: Amount? = null
)