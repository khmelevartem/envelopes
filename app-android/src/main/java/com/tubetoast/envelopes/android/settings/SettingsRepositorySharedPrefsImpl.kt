package com.tubetoast.envelopes.android.settings

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import com.tubetoast.envelopes.common.settings.MutableSettingsRepository
import com.tubetoast.envelopes.common.settings.Setting
import com.tubetoast.envelopes.common.settings.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositorySharedPrefsImpl(
    private val sharedPrefs: SharedPreferences,
    private val defaultSettingsRepo: SettingsRepository
) : MutableSettingsRepository {

    constructor(
        context: Context,
        defaultSettingsRepo: SettingsRepository
    ) : this(context.getSharedPreferences("SettingsRepository", MODE_PRIVATE), defaultSettingsRepo)

    private val flows = mutableMapOf<Setting.Key, MutableStateFlow<Setting>>()

    override val settings: List<Setting>
        get() = Setting.Key.entries.map { getSetting(it) }

    override fun getSetting(key: Setting.Key): Setting {
        val default = defaultSettingsRepo.getSetting(key)
        val savedIsChecked = sharedPrefs.getBoolean(key.name, default.checked)
        return default.copy(checked = savedIsChecked)
    }

    override fun getSettingFlow(key: Setting.Key): StateFlow<Setting> = flows.getOrPut(key) {
        MutableStateFlow(getSetting(key))
    }.asStateFlow()

    override fun saveChanges(settings: List<Setting>) {
        saveChanges(*settings.toTypedArray())
    }

    override fun saveChanges(vararg settings: Setting) {
        sharedPrefs.edit {
            settings.forEach {
                putBoolean(it.key.name, it.checked)
                flows.getOrPut(it.key) { MutableStateFlow(it) }.tryEmit(it)
            }
        }
    }
}
