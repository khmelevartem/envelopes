package com.tubetoast.envelopes.common.domain.models

data class Envelope(
    val name: String,
    val limit: Amount,
) : ImmutableModel<Envelope>() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Envelope

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    companion object {
        val EMPTY = Envelope(name = "", limit = Amount.ZERO)
    }
}
