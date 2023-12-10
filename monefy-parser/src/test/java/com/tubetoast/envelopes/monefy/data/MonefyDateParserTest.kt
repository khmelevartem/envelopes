package com.tubetoast.envelopes.monefy.data

import com.tubetoast.envelopes.common.domain.models.Date
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MonefyDateParserTest {
    @Test
    fun parse() {
        val realString = "01/01/2019"
        val actual = MonefyDateParser().parseDate(realString)
        val expected = Date(
            minute = 0,
            hour = 0,
            day = 1,
            month = 1,
            year = 2019,
        )
        Assertions.assertEquals(expected, actual)
    }
}
