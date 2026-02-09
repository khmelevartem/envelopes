package com.tubetoast.envelopes.common.domain.models

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe

class DateTest :
    FunSpec({

        test("compare") {
            val date1 = Date(day = 1, month = 1, year = 1984)
            val date2 = Date(day = 2, month = 1, year = 1984)
            val date3 = Date(day = 1, month = 2, year = 1984)
            val date4 = Date(day = 1, month = 1, year = 1985)
            val date5 = Date(day = 1, month = 1, year = 1985)

            date1 shouldBeLessThan date2
            date1 shouldBeLessThan date3
            date1 shouldBeLessThan date4
            date2 shouldBeLessThan date3
            date2 shouldBeLessThan date4
            date3 shouldBeLessThan date4
            date4 shouldBe date5
        }

        test("init") {
            shouldNotThrow<Exception> {
                Date(day = 1, month = 1, year = 1)
                Date(day = 1, month = 1, year = -1)
                Date(day = 31, month = 12, year = 2000)
                Date(day = 30, month = 11, year = 2)
                Date(day = 29, month = 4, year = 2000)
            }

            shouldThrow<IllegalArgumentException> {
                Date(day = 29, month = 2, year = 2001)
            }
            shouldThrow<IllegalArgumentException> {
                Date(day = 31, month = 2, year = 2001)
            }
            shouldThrow<IllegalArgumentException> {
                Date(day = 32, month = 1, year = 2001)
            }
        }
    })
