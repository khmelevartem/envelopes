package com.tubetoast.envelopes.monefy

data class MonefyDataColumns(
    val date: Int,
    val category: Int,
    val amount: Int,
    val description: Int,
    val size: Int
) {
    companion object Parser {
        private const val KEY_DATE = "date"
        private const val KEY_CATEGORY = "category"
        private const val KEY_AMOUNT = "amount"
        private const val KEY_DESCRIPTION = "description"
        fun parse(titlesLine: String): MonefyDataColumns {
            return titlesLine.split(MonefyDataParser.DELIMITER).let { titles ->
                MonefyDataColumns(
                    date = titles.getColumnNumber(KEY_DATE),
                    category = titles.getColumnNumber(KEY_CATEGORY),
                    amount = titles.getColumnNumber(KEY_AMOUNT),
                    description = titles.getColumnNumber(KEY_DESCRIPTION),
                    size = titles.size
                )
            }
        }

        private fun List<String>.getColumnNumber(key: String) =
            indexOf(key)
                .takeIf { it >= 0 }
                ?: throw IllegalArgumentException("$key column not found in $this")
    }
}
