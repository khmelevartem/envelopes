package com.tubetoast.envelopes.monefy.data

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Transaction
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import java.io.File

class MonefyDataParser(
    private val dateParser: MonefyDateParser = MonefyDateParser(),
    private val operationParser: MonefyTransactionParser = MonefyTransactionParser()
) {

    fun parse(lines: List<String>, startFrom: Date? = null): List<CategorySnapshot> {
        val snapshots: MutableMap<String, MutableList<Transaction<*>>> = mutableMapOf()

        val columns = MonefyDataColumns.parse(lines.first())
        lines.subList(1, lines.size).forEach { line ->
            line.process(columns, snapshots, startFrom)
        }
        return snapshots.map { (key, value) ->
            CategorySnapshot(category = Category(name = key), transactions = value)
        }
    }

    fun parse(file: File, startFrom: Date? = null): List<CategorySnapshot> {
        file.reader(Charsets.UTF_8).use { reader ->
            return parse(reader.readLines(), startFrom)
        }
    }

    private fun String.process(
        columns: MonefyDataColumns,
        snapshots: MutableMap<String, MutableList<Transaction<*>>>,
        startFrom: Date?
    ) {
        val values = split(DELIMITER).takeIf { it.size == columns.size }
            ?: throw IllegalArgumentException("wtf? $this")
        val category = values[columns.category]
        val date = dateParser.parseDate(values[columns.date])
        if (startFrom != null && date < startFrom) return
        snapshots.getOrPut(category) { mutableListOf() }.add(
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
