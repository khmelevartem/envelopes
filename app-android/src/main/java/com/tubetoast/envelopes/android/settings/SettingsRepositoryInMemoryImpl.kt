package com.tubetoast.envelopes.android.settings

abstract class SettingsRepositoryInMemoryImpl : SettingsRepository {

    protected abstract val defaultSettings: MutableMap<String, Setting>

    override val settings: List<Setting> get() = defaultSettings.values.toList()

    override fun saveChanges(settings: List<Setting>) {
        settings.forEach {
            defaultSettings[it.text] = it
        }
    }

    protected fun createSettings(vararg settings: Pair<String, Boolean>) =
        mutableMapOf(
            *settings.map { it.first to Setting(it.first, it.second) }.toTypedArray()
        )

    protected fun createSettings(vararg settings: Setting) =
        mutableMapOf(
            *settings.map { it.text to it }.toTypedArray()
        )
}