package com.tubetoast.envelopes.common.domain.models

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe

class DateRangeTest :
    FunSpec({

        test("testInRange") {
            val dateRange = Date(1, 1, 1)..Date(2, 2, 2)
            val dateInRange = Date(1, 2, 1)
            (dateInRange in dateRange).shouldBeTrue()
        }

        test("testNotInRange") {
            val dateRange = Date(1, 1, 1)..Date(2, 2, 2)
            val dateNotInRange = Date(3, 2, 2)
            (dateNotInRange !in dateRange).shouldBeTrue()
        }

        context("testCompare") {
            withData(
                Date(1, 1, 1)..Date(2, 2, 2) to Date(3, 2, 2)..Date(3, 3, 3),
                Date(1, 1, 1)..Date(2, 2, 2) to Date(1, 2, 1)..Date(3, 3, 3)
            ) { (range1, range2) ->
                range1 shouldBeLessThan range2
                range2 shouldBeGreaterThan range1
            }
        }

        test("testCompareEquals") {
            val range1 = Date(1, 1, 1)..Date(3, 3, 3)
            val range2 = Date(1, 1, 1)..Date(3, 3, 3)
            range1 shouldBe range2
        }

        test("testCannotCompare") {
            val range1 = Date(1, 1, 1)..Date(5, 5, 5)
            val range2 = Date(2, 2, 2)..Date(3, 3, 3)
            shouldThrow<IllegalStateException> {
                range1 < range2
            }
        }
    })
