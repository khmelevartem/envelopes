package com.tubetoast.envelopes.monefy.data

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MonefyDataColumnsTest {

    @Test
    fun parse() {
        val realString =
            "date;account;category;amount;currency;converted amount;currency;description"
        MonefyDataColumns.parse(realString).apply {
            Assertions.assertEquals(0, date)
            Assertions.assertEquals(2, category)
            Assertions.assertEquals(3, amount)
            Assertions.assertEquals(7, description)
            Assertions.assertEquals(8, size)
        }
    }
}
