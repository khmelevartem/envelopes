package com.tubetoast.envelopes.common.domain.models

data class Envelope(
    val name: String,
    val limit: Amount
) : ImmutableModel {

    override val id = "$className$name"

    companion object {
        val EMPTY = Envelope(name = "", limit = Amount.ZERO)
    }
}
