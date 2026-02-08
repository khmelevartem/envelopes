package com.tubetoast.envelopes.monefy.data

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Transaction
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot

class MonefyDataParser(
    private val dateParser: MonefyDateParser = MonefyDateParser(),
    private val operationParser: MonefyTransactionParser = MonefyTransactionParser()
) {
    fun parse(
        lines: List<String>,
        startFrom: Date? = null
    ): List<CategorySnapshot> {
        if (lines.isEmpty()) return emptyList()

        val snapshots: MutableMap<String, MutableSet<Transaction<*>>> = mutableMapOf()
        val columns = MonefyDataColumns.parse(lines.first())

        lines.drop(1).forEach { line ->
            if (line.isNotBlank()) {
                line.process(columns, snapshots, startFrom)
            }
        }
        return snapshots.map { (key, value) ->
            CategorySnapshot(category = Category(name = key), transactions = value)
        }
    }

    private fun String.process(
        columns: MonefyDataColumns,
        snapshots: MutableMap<String, MutableSet<Transaction<*>>>,
        startFrom: Date?
    ) {
        val values = split(DELIMITER).takeIf { it.size == columns.size }
            ?: return // Skip malformed lines instead of crashing

        val category = values[columns.category]
        val date = dateParser.parseDate(values[columns.date])

        if (startFrom != null && date <= startFrom) return

        snapshots.getOrPut(category) { mutableSetOf() }.add(
            operationParser.parse(
                string = values[columns.amount],
                date = date,
                comment = values[columns.description].trim()
            )
        )
    }

    companion object {
        const val DELIMITER = ";"
        const val DATE_DELIMITER = "/"
        const val DECIMAL_DELIMITER = "."
    }
}

fun throwNFE(it: String): Nothing = throw NumberFormatException("$it is not a number")
