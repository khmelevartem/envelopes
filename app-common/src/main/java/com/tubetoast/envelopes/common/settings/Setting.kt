package com.tubetoast.envelopes.common.settings

data class Setting(
    val key: Key,
    val text: String,
    val checked: Boolean
) {
    enum class Key {
        FROM_LAST_IMPORT, FILTER_BY_YEAR, DELETE_SPENDING
    }
}

fun Setting.Key.default(): Setting =
    when (this) {
        Setting.Key.FROM_LAST_IMPORT -> Setting(this, "From last import", true)
        Setting.Key.FILTER_BY_YEAR -> Setting(this, "Filter by year", false)
        // это костыль. true - одноразово удалить все. false - ничего
        Setting.Key.DELETE_SPENDING -> Setting(this, "Delete all spending", false)
    }
