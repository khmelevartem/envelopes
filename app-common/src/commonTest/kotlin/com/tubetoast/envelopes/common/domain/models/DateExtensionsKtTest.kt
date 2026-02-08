package com.tubetoast.envelopes.common.domain.models

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class DateExtensionsKtTest :
    FunSpec({

        context("testDaysOfYear") {
            withData(
                nameFn = { "Day ${it.first} for date ${it.second}" },
                1 to Date(1, 1, 1),
                2 to Date(2, 1, 145),
                32 to Date(1, 2, 33),
                33 to Date(2, 2, 2),
                60 to Date(1, 3, 1),
                61 to Date(1, 3, 4),
                365 to Date(31, 12, 5),
                366 to Date(31, 12, 44)
            ) { (expectedDay, date) ->
                date.inDayOfYear() shouldBe expectedDay
            }
        }

        context("testInMonths") {
            data class MonthTestData(
                val start: Date,
                val end: Date,
                val months: Int
            )
            withData(
                nameFn = { "${it.months} from ${it.start} to ${it.end}" },
                MonthTestData(Date(1, 1, 1), Date(2, 1, 1), 0),
                MonthTestData(Date(1, 1, 1), Date(1, 2, 1), 1),
                MonthTestData(Date(2, 1, 1), Date(1, 2, 1), 1),
                MonthTestData(Date(2, 1, 1), Date(1, 3, 1), 1),
                MonthTestData(Date(30, 1, 1), Date(1, 3, 1), 1),
                MonthTestData(Date(1, 1, 1), Date(1, 1, 2), 12),
                MonthTestData(Date(1, 1, 1), Date(2, 1, 2), 12),
                MonthTestData(Date(2, 1, 1), Date(1, 2, 2), 13),
                MonthTestData(Date(1, 1, 1), Date(2, 2, 2), 13),
                MonthTestData(Date(1, 12, 1), Date(1, 1, 2), 1),
                MonthTestData(Date(31, 12, 1), Date(1, 1, 2), 0),
                MonthTestData(Date(1, 1, 2019), Date(28, 12, 2024), 72),
                MonthTestData(Date(1, 1, 2019), Date(1, 1, 2021), 24),
                MonthTestData(Date(1, 1, 2019), Date(1, 1, 2022), 36)
            ) { (start, end, expectedMonths) ->
                (start..end).inMonths() shouldBe expectedMonths
            }
        }
    })
