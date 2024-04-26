package com.tubetoast.envelopes.android.settings

class SettingsRepositoryInMemoryImpl : SettingsRepository {

    private val _settings = createSettings(
        Setting("Test setting", false)
    )

    override val settings: List<Setting> get() = _settings.values.toList()

    override fun saveChanges(settings: List<Setting>) {
        settings.forEach {
            _settings[it.text] = it
        }
    }

    private fun createSettings(vararg settings: Setting) =
        mutableMapOf(
            *settings.map { it.text to it }.toTypedArray()
        )
}