package com.tubetoast.envelopes.android.settings

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class SettingsRepositorySharedPrefsImpl(
    private val sharedPrefs: SharedPreferences,
    private val defaultSettingsRepo: SettingsRepository
) : MutableSettingsRepository {

    constructor(
        context: Context,
        defaultSettingsRepo: SettingsRepository
    ) : this(context.getSharedPreferences("SettingsRepository", MODE_PRIVATE), defaultSettingsRepo)

    override val settings: List<Setting>
        get() = mutableListOf<Setting>().apply {
            defaultSettingsRepo.settings.forEach { default ->
                add(
                    default.copy(
                        checked = sharedPrefs.getBoolean(default.key.name, default.checked)
                    )
                )
            }
        }.toList()

    override fun getSetting(key: Setting.Key): Setting {
        val default = defaultSettingsRepo.getSetting(key)
        val savedIsChecked = sharedPrefs.getBoolean(key.name, default.checked)
        return default.copy(checked = savedIsChecked)
    }

    override fun saveChanges(settings: List<Setting>) {
        val editor = sharedPrefs.edit()
        settings.forEach {
            editor.putBoolean(it.key.name, it.checked)
        }
        editor.apply()
    }
}