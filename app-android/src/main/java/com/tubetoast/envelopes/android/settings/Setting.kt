package com.tubetoast.envelopes.android.settings


data class Setting(
    val key: Key,
    val text: String,
    val checked: Boolean
) {
    enum class Key {
        CURRENT_MONTH
    }
}

fun Setting.Key.default(): Setting =
    when (this) {
        Setting.Key.CURRENT_MONTH -> Setting(this, "Current month", true)
    }
