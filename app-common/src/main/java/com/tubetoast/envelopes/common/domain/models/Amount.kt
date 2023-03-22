package com.tubetoast.envelopes.common.domain.models

data class Amount(
    val units: Int,
    val shares: Int = 0,
    val currency: Currency = Currency.Ruble,
) {
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
            currency = this.currency,
        )
    }

    infix operator fun div(another: Amount): Float {
        checkCurrency(another)
        return this.inShares().toFloat() / another.inShares()
    }

    fun inShares(): Int = this.units * this.currency.shares + this.shares

    private fun checkCurrency(another: Amount) {
        check(this.currency == another.currency) {
            "Need to use converter to sum $this and $another"
        }
    }
}

fun Iterable<Amount>.sum(): Amount = sumOf { it }

inline fun Iterable<Amount>.sumOf(selector: (Amount) -> Amount): Amount {
    var sum = Amount(0)
    for (element in this) {
        sum += selector(element)
    }
    return sum
}