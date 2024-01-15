package com.tubetoast.envelopes.monefy.data

import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.monefy.data.MonefyDataParser.Companion.DATE_DELIMITER

class MonefyDateParser {
    fun parseDate(it: String): Date {
        val (day, month, year) = it.split(DATE_DELIMITER).map {
            it.toIntOrNull() ?: throwNFE("date component $it")
        }
        return Date(
            day = day,
            month = month,
            year = year

        )
    }
}
