package com.tubetoast.envelopes.monefy.data

import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Earning
import com.tubetoast.envelopes.common.domain.models.Spending
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class MonefyTransactionParserTest :
    FunSpec({
        val parser = MonefyTransactionParser()
        val testDate = Date(1, 1, 2024)
        val comment = "comment"

        test("parse spending with negative sign and space") {
            val realString = "-2 500" // contains non-breaking space
            val actual = parser.parse(realString, testDate, comment)

            actual shouldBe Spending(
                amount = Amount(2500),
                date = testDate,
                comment = comment
            )
        }

        test("parse earning with decimal") {
            val realString = "2500.2"
            val actual = parser.parse(realString, testDate, comment)

            actual shouldBe Earning(
                amount = Amount(units = 2500, shares = 2),
                date = testDate,
                comment = comment
            )
        }

        test("parse zero amount") {
            val realString = "0"
            val actual = parser.parse(realString, testDate, "none")
            actual.amount shouldBe Amount.ZERO
        }
    })
