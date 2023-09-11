package com.tubetoast.envelopes.common.domain.models

data class Envelope(
    val name: String,
    val limit: Amount,
) : ImmutableModel<Envelope>() {
    companion object {
        val EMPTY = Envelope(name = "", limit = Amount.ZERO)
    }
}
