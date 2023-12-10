package com.tubetoast.envelopes.monefy.data

import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Earning
import com.tubetoast.envelopes.common.domain.models.Spending
import com.tubetoast.envelopes.common.domain.models.randomDate
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MonefyTransactionParserTest {
    private val parser = MonefyTransactionParser()

    @Test
    fun parseSpending() {
        val date = randomDate()
        val comment = "comment"
        val realString = "-2 500"
        val actual = parser.parse(
            realString,
            date,
            comment,
        )
        val expected = Spending(
            amount = Amount(2500),
            date = date,
            comment = comment,
        )
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun parseEarning() {
        val date = randomDate()
        val comment = "comment"
        val realString = "2500.2"
        val actual = parser.parse(
            realString,
            date,
            comment,
        )
        val expected = Earning(
            amount = Amount(units = 2500, shares = 2),
            date = date,
            comment = comment,
        )
        Assertions.assertEquals(expected, actual)
    }
}
