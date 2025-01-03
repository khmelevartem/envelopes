package com.tubetoast.envelopes.common.domain.models

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class DateRangeTest {
    @Test
    fun testInRange() {
        val dateRange = Date(1, 1, 1)..Date(2, 2, 2)
        val dateInRange = Date(1, 2, 1)
        Assertions.assertTrue(dateInRange in dateRange)
    }

    @Test
    fun testNotInRange() {
        val dateRange = Date(1, 1, 1)..Date(2, 2, 2)
        val dateNotInRange = Date(1, 2, 2)
        Assertions.assertFalse(dateNotInRange !in dateRange)
    }

    @ParameterizedTest
    @MethodSource("provideData")
    fun testCompare(range1: DateRange, range2: DateRange) {
        Assertions.assertTrue(range1 < range2)
        Assertions.assertTrue(range2 > range1)
    }

    @Test
    fun testCompareEquals() {
        val range1 = Date(1, 1, 1)..Date(3, 3, 3)
        val range2 = Date(1, 1, 1)..Date(3, 3, 3)
        Assertions.assertTrue(range1 == range2)
    }

    @Test
    fun testCannotCompare() {
        val range1 = Date(1, 1, 1)..Date(5, 5, 5)
        val range2 = Date(2, 2, 2)..Date(3, 3, 3)
        Assertions.assertThrows(IllegalStateException::class.java) {
            range1 < range2
        }
    }

    companion object {
        @JvmStatic
        fun provideData(): Array<Arguments> {
            return arrayOf(
                Arguments.of(
                    Date(1, 1, 1)..Date(2, 2, 2),
                    Date(3, 2, 2)..Date(3, 3, 3)
                ),
                Arguments.of(
                    Date(1, 1, 1)..Date(2, 2, 2),
                    Date(1, 2, 1)..Date(3, 3, 3)
                )
            )
        }
    }
}
