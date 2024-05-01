package com.tubetoast.envelopes.common.domain.models

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DateTest {
    @Test
    fun compare() {
        val date1 = Date(day = 1, month = 1, year = 1984)
        val date2 = Date(day = 2, month = 1, year = 1984)
        val date3 = Date(day = 1, month = 2, year = 1984)
        val date4 = Date(day = 1, month = 1, year = 1985)
        val date5 = Date(day = 1, month = 1, year = 1985)
        Assertions.assertTrue(date1 < date2)
        Assertions.assertTrue(date1 < date3)
        Assertions.assertTrue(date1 < date4)
        Assertions.assertTrue(date2 < date3)
        Assertions.assertTrue(date2 < date4)
        Assertions.assertTrue(date3 < date4)
        Assertions.assertTrue(date4 == date5)
    }

    @Test
    fun init() {
        Assertions.assertDoesNotThrow {
            Date(day = 1, month = 1, year = 1)
            Date(day = 1, month = 1, year = -1)
            Date(day = 31, month = 12, year = 2000)
            Date(day = 30, month = 11, year = 2)
            Date(day = 29, month = 4, year = 2000)
        }
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            Date(day = 29, month = 2, year = 2001)
        }
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            Date(day = 31, month = 2, year = 2001)
        }
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            Date(day = 32, month = 1, year = 2001)
        }
    }
}