package com.tubetoast.envelopes.android

import android.content.Context
import androidx.core.content.edit
import com.tubetoast.envelopes.ui.presentation.LocalKVStore

class SharedPrefsLocalKVStore(
    context: Context,
    name: String
) : LocalKVStore {
    private val prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    override fun getString(key: String) = prefs.getString(key, null)

    override fun putString(
        key: String,
        value: String?
    ) = prefs.edit { putString(key, value) }

    override fun getBoolean(
        key: String,
        default: Boolean
    ) = prefs.getBoolean(key, default)

    override fun putBoolean(
        key: String,
        value: Boolean
    ) = prefs.edit { putBoolean(key, value) }
}
