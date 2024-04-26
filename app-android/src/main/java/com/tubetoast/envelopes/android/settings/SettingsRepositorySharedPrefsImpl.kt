package com.tubetoast.envelopes.android.settings

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class SettingsRepositorySharedPrefsImpl(
    private val sharedPrefs: SharedPreferences,
    private val defaultSettingsRepo: SettingsRepository
) : SettingsRepository {

    constructor(
        context: Context,
        defaultSettingsRepo: SettingsRepository
    ) : this(context.getSharedPreferences("SettingsRepository", MODE_PRIVATE), defaultSettingsRepo)

    override val settings: List<Setting>
        get() = mutableListOf<Setting>().apply {
            defaultSettingsRepo.settings.forEach { default ->
                add(
                    default.copy(
                        checked = sharedPrefs.getBoolean(default.text, default.checked)
                    )
                )
            }
        }.toList()

    override fun saveChanges(settings: List<Setting>) {
        val editor = sharedPrefs.edit()
        settings.forEach {
            editor.putBoolean(it.text, it.checked)
        }
        editor.apply()
    }
}