package com.tubetoast.envelopes.android.settings

open class SettingsRepositoryDefaultImpl : SettingsRepository {

    protected val defaultSettings: MutableMap<Setting.Key, Setting>
        get() = createSettings(Setting.Key.entries.map { it.default() })

    override val settings: List<Setting> get() = defaultSettings.values.toList()

    override fun getSetting(key: Setting.Key): Setting =
        (defaultSettings[key] ?: throw IllegalStateException("Setting for key $key not found"))

    private fun createSettings(settings: List<Setting>): MutableMap<Setting.Key, Setting> =
        mutableMapOf(
            *settings.map { it.key to it }.toTypedArray()
        )
}