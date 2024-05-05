package com.tubetoast.envelopes.common.settings

data class Setting(
    val key: Key,
    val text: String,
    val checked: Boolean
) {
    enum class Key {
        FROM_LAST_IMPORT, FILTER_BY_YEAR
    }
}

fun Setting.Key.default(): Setting =
    when (this) {
        Setting.Key.FROM_LAST_IMPORT -> Setting(this, "From last import", true)
        Setting.Key.FILTER_BY_YEAR -> Setting(this, "Filter by year", false)
    }
