package com.tubetoast.envelopes.ui.presentation

interface LocalKVStore {
    fun getString(key: String): String?

    fun putString(
        key: String,
        value: String?
    )

    fun getBoolean(
        key: String,
        default: Boolean
    ): Boolean

    fun putBoolean(
        key: String,
        value: Boolean
    )
}
