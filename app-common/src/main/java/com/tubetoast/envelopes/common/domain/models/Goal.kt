package com.tubetoast.envelopes.common.domain.models

data class Goal(
    val name: String,
    val target: Amount,
    val categories: Set<Category>,
    val start: Date? = null,
    val finish: Date? = null
) : ImmutableModel<Goal>() {
    companion object {
        val EMPTY = Goal(name = "", target = Amount.ZERO, categories = emptySet())
    }
}
