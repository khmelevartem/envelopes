package com.tubetoast.envelopes.monefy.data

import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Earning
import com.tubetoast.envelopes.common.domain.models.Spending
import com.tubetoast.envelopes.common.domain.models.Transaction
import com.tubetoast.envelopes.monefy.data.MonefyDataParser.Companion.DECIMAL_DELIMITER
import kotlin.math.abs

class MonefyTransactionParser {
    fun parse(string: String, date: Date, comment: String?): Transaction<*> {
        val number = string.replace(" ", "")
            .split(DECIMAL_DELIMITER)
            .map { it.toIntOrNull() ?: throwNFE("amount $string") }
        val amount = Amount(units = abs(number.first()), number.getOrElse(1) { 0 })
        return if (number.first() > 0) {
            Earning(
                amount = amount,
                date = date,
                comment = comment,
            )
        } else {
            Spending(
                amount = amount,
                date = date,
                comment = comment,
            )
        }
    }
}
