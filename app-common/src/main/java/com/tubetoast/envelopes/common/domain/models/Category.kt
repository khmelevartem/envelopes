package com.tubetoast.envelopes.common.domain.models

data class Category(
    val name: String,
    val limit: Amount? = null
) : ImmutableModel {
    override val id: String = "$className:$name"

    companion object {
        val EMPTY = Category("")
    }

}
