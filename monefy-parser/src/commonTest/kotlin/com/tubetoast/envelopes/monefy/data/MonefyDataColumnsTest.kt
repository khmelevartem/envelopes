package com.tubetoast.envelopes.monefy.data

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class MonefyDataColumnsTest :
    FunSpec({

        test("parse valid column string") {
            val realString = "date;account;category;amount;currency;converted amount;currency;description"

            with(MonefyDataColumns.parse(realString)) {
                date shouldBe 0
                category shouldBe 2
                amount shouldBe 3
                description shouldBe 7
                size shouldBe 8
            }
        }

        test("parse columns with different order") {
            val mixedString = "category;amount;date;description"
            with(MonefyDataColumns.parse(mixedString)) {
                category shouldBe 0
                amount shouldBe 1
                date shouldBe 2
                description shouldBe 3
                size shouldBe 4
            }
        }
    })
