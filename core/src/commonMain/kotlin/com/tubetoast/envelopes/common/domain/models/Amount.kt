package com.tubetoast.envelopes.common.domain.models

import kotlin.math.roundToLong

data class Amount(
    val units: Long,
    val shares: Int = 0,
    val currency: Currency = Currency.Ruble
) : ImmutableModel<Amount>(),
    Comparable<Amount> {
    init {
        check(units >= 0 && shares >= 0) {
            "Cannot create negative amount $this"
        }
    }

    infix operator fun plus(another: Amount): Amount {
        checkCurrency(another)
        val sharesSum = this.shares + another.shares
        val shares = sharesSum % SHARES_IN_UNIT
        val plusUnits = sharesSum.floorDiv(SHARES_IN_UNIT)
        return Amount(
            units = this.units + another.units + plusUnits,
            shares = shares,
            currency = this.currency
        )
    }

    infix operator fun minus(another: Amount): Amount {
        checkCurrency(another)
        val sharesDiff = this.shares - another.shares
        val shares = if (sharesDiff >= 0) sharesDiff else SHARES_IN_UNIT + sharesDiff
        val minusUnits = if (sharesDiff >= 0) 0 else 1
        return Amount(
            units = this.units - another.units - minusUnits,
            shares = shares,
            currency = this.currency
        )
    }

    infix operator fun div(another: Amount): Float {
        checkCurrency(another)
        return this.units.toFloat() / another.units // avoid shared not to overflow int
    }

    infix operator fun div(times: Int): Amount =
        copy(units = (this.units.toFloat() / times).roundToLong()) // avoid shared not to overflow int

    infix operator fun times(other: Int) =
        run {
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

    override fun compareTo(other: Amount): Int {
        checkCurrency(other)
        return when {
            this.units > other.units -> 1
            other.units > this.units -> -1
            this.shares > other.shares -> 1
            other.shares > this.shares -> -1
            else -> 0
        }
    }

    companion object {
        private const val SHARES_IN_UNIT = 100
        val ZERO = Amount(0)
    }
}

fun Iterable<Amount>.summarize(): Amount = sumOf { it }

fun Sequence<Amount>.summarize(): Amount = sumOf { it }

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
