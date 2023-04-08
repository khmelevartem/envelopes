package com.tubetoast.envelopes.common.domain.models

data class Category(
    val name: String,
    val limit: Amount? = null
) : ImmutableModel<Category>()