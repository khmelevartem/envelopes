package com.tubetoast.envelopes.monefy.data

import com.tubetoast.envelopes.common.domain.models.Date
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class MonefyDateParserTest :
    FunSpec({
        val parser = MonefyDateParser()

        test("parse valid date format DD/MM/YYYY") {
            parser.parseDate("31/12/2023") shouldBe Date(31, 12, 2023)
        }

        test("handle single digit days and months") {
            parser.parseDate("1/1/2024") shouldBe Date(1, 1, 2024)
        }

        test("handle leading zeros") {
            parser.parseDate("05/05/2024") shouldBe Date(5, 5, 2024)
        }
    })
