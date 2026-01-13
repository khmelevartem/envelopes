package com.tubetoast.envelopes.common.domain.models

data class Amount(
    val units: Long,
    val shares: Int = 0,
    val currency: Currency = Currency.Ruble
) : ImmutableModel<Amount>() {
    init {
        check(units >= 0 && shares >= 0) {
            "Cannot create negative amount $this"
        }
    }

    infix operator fun plus(another: Amount): Amount {
        checkCurrency(another)
        return Amount(
            units = this.units + another.units,
            shares = this.shares + another.shares,
            currency = this.currency
        )
    }

    infix operator fun div(another: Amount): Float {
        checkCurrency(another)
        return this.units.toFloat() / another.units // avoid shared not to overflow int
    }

    infix operator fun times(other: Int) = run {
        val (units, shares) = (inShares() * other)
            .let { it / currency.shares to it.mod(currency.shares) }
        copy(units = units, shares = shares)
    }

    private fun inShares(): Long = units * currency.shares + shares

    private fun checkCurrency(another: Amount) {
        check(this.currency == another.currency) {
            "Need to use converter to sum $this and $another"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Amount) return false

        if (units != other.units) return false
        if (shares != other.shares) return false
        if (currency != other.currency) return false

        return true
    }

    override fun hashCode(): Int {
        var result = units.hashCode()
        result = 31 * result + shares
        result = 31 * result + currency.name.hashCode()
        return result
    }

    companion object {
        val ZERO = Amount(0)
    }
}

fun Iterable<Amount>.sum(): Amount = sumOf { it }
fun Sequence<Amount>.sum(): Amount = sumOf { it }

inline fun Iterable<Amount>.sumOf(selector: (Amount) -> Amount): Amount {
    var sum = Amount.ZERO
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

inline fun Sequence<Amount>.sumOf(selector: (Amount) -> Amount): Amount {
    var sum = Amount.ZERO
    for (element in this) {
        sum += selector(element)
    }
    return sum
}
