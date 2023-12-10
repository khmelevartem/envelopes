package com.tubetoast.envelopes.monefy

import com.tubetoast.envelopes.common.domain.models.Operation
import java.io.File

class MonefyDataParser(
    private val file: File,
    private val dateParser: MonefyDateParser = MonefyDateParser(),
    private val operationParser: MonefyOperationParser = MonefyOperationParser(),
) {

    fun parse(): List<Operation> {
        return file.reader(Charsets.UTF_8).use { reader ->
            val lines = reader.readLines()
            val columns = MonefyDataColumns.Parser.parse(lines.first())
            lines.subList(1, lines.size).mapNotNull { line ->
                line.split(DELIMITER).takeIf { it.size == columns.size }?.let { values ->
                    operationParser.parse(
                        string = values[columns.amount],
                        date = dateParser.parseDate(values[columns.date]),
                        comment = values[columns.description].trim(),
                    )
                } ?: throw IllegalArgumentException("wtf? $line")
            }
        }
    }

    companion object {
        const val DELIMITER = ";"
        const val DATE_DELIMITER = "/"
        const val DECIMAL_DELIMITER = "."
    }
}

fun throwNFE(it: String): Nothing = throw NumberFormatException("$it is not a number")
