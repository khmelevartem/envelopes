package com.tubetoast.envelopes.android.settings


data class Setting(
    val key: Key,
    val text: String,
    val checked: Boolean
) {
    enum class Key {
        FROM_LAST_IMPORT
    }
}

fun Setting.Key.default(): Setting =
    when (this) {
        Setting.Key.FROM_LAST_IMPORT -> Setting(this, "From Last Import", true)
    }
