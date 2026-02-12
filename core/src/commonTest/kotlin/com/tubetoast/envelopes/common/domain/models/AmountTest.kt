package com.tubetoast.envelopes.common.domain.models

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class AmountTest :
    FunSpec({

        test("testSumOf") {
            listOf(
                Amount(5),
                Amount(2),
                Amount(3),
                Amount.ZERO
            ).summarize() shouldBe Amount(10)
        }

        test("testDiv") {
            val first = Amount(units = 20683720, shares = 18, currency = Currency.Ruble)
            val second = Amount(units = 50_000_000, shares = 0, currency = Currency.Ruble)
            (first / second) shouldBe (20683720f / 50_000_000)
        }

        test("testTimes") {
            val first = Amount(units = 3, shares = 55)
            (first * 3) shouldBe Amount(units = 10, shares = 65)

            val second = Amount(3)
            (second * 4) shouldBe Amount(units = 12)
        }
    })
