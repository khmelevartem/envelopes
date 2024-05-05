package com.tubetoast.envelopes.common.settings

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class SettingsRepositoryDefaultImpl : SettingsRepository {

    protected val defaultSettings: MutableMap<Setting.Key, Setting> by lazy {
        createSettings(Setting.Key.entries.map { it.default() })
    }

    override val settings: List<Setting> by lazy {
        defaultSettings.values.toList()
    }

    override fun getSetting(key: Setting.Key): Setting =
        (defaultSettings[key] ?: throw IllegalStateException("Setting for key $key not found"))

    override fun getSettingFlow(key: Setting.Key): StateFlow<Setting> =
        MutableStateFlow(getSetting(key))

    private fun createSettings(settings: List<Setting>): MutableMap<Setting.Key, Setting> =
        mutableMapOf(
            *settings.map { it.key to it }.toTypedArray()
        )
}