package com.tubetoast.envelopes.monefy.data

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Transaction
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import java.io.File

class MonefyDataParser(
    private val dateParser: MonefyDateParser = MonefyDateParser(),
    private val operationParser: MonefyTransactionParser = MonefyTransactionParser(),
) {

    fun parse(file: File): List<CategorySnapshot> {
        val snapshots: MutableMap<String, MutableSet<Transaction>> = mutableMapOf()

        file.reader(Charsets.UTF_8).use { reader ->
            val lines = reader.readLines()
            val columns = MonefyDataColumns.parse(lines.first())
            lines.subList(1, lines.size).forEach { line ->
                line.process(columns, snapshots)
            }
            return snapshots.map { (key, value) ->
                CategorySnapshot(category = Category(name = key), transactions = value)
            }
        }
    }

    private fun String.process(
        columns: MonefyDataColumns,
        snapshots: MutableMap<String, MutableSet<Transaction>>,
    ) {
        val values = split(DELIMITER).takeIf { it.size == columns.size }
            ?: throw IllegalArgumentException("wtf? $this")
        val category = values[columns.category]
        snapshots.getOrPut(category) { mutableSetOf() }.add(
            operationParser.parse(
                string = values[columns.amount],
                date = dateParser.parseDate(values[columns.date]),
                comment = values[columns.description].trim(),
            ),
        )
    }

    companion object {
        const val DELIMITER = ";"
        const val DATE_DELIMITER = "/"
        const val DECIMAL_DELIMITER = "."
    }
}

fun throwNFE(it: String): Nothing = throw NumberFormatException("$it is not a number")
