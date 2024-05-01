package com.tubetoast.envelopes.common.domain.models

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

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

}