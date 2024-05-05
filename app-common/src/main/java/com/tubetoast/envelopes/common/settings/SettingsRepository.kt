package com.tubetoast.envelopes.common.settings

import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: List<Setting>
    fun getSetting(key: Setting.Key): Setting
    fun getSettingFlow(key: Setting.Key): StateFlow<Setting>
}

interface MutableSettingsRepository : SettingsRepository {
    fun saveChanges(settings: List<Setting>)
}