package com.tubetoast.envelopes.common.domain.models

data class Envelope(
    val name: String,
    val limit: Amount
) : ImmutableModel<Envelope>() {

    override val id: Id<Envelope> = name.id()

    val yearLimit = limit * 12

    companion object {
        val EMPTY = Envelope(name = "", limit = Amount.ZERO)
    }
}

val undefinedCategoriesEnvelope =
    Envelope(name = "Undefined", limit = Amount.ZERO)
