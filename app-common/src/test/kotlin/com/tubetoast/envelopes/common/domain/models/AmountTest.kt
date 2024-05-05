package com.tubetoast.envelopes.common.domain.models

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AmountTest {
    @Test
    fun testSumOf() {
        listOf(
            Amount(5),
            Amount(2),
            Amount(3),
            Amount.ZERO
        ).sum().let {
            assertThat(it).isEqualTo(Amount(10))
        }
    }

    @Test
    fun testDiv() {
        val first = Amount(units = 20683720, shares = 18, currency = Currency.Ruble)
        val second = Amount(units = 50_000_000, shares = 0, currency = Currency.Ruble)
        assertThat(first / second).isEqualTo(20683720f / 50_000_000)
    }

    @Test
    fun testTimes() {
        val first = Amount(units = 3, shares = 55)
        Assertions.assertEquals(Amount(units = 10, shares = 65), first * 3)

        val second = Amount(3)
        Assertions.assertEquals(Amount(units = 12), second * 4)
    }
}
