package com.tubetoast.envelopes.android.settings

interface SettingsRepository {
    val settings: List<Setting>
    fun getSetting(key: Setting.Key): Setting
}

interface MutableSettingsRepository : SettingsRepository {
    fun saveChanges(settings: List<Setting>)
}