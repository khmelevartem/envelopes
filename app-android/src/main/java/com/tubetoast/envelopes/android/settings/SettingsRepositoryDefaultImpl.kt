package com.tubetoast.envelopes.android.settings

class SettingsRepositoryDefaultImpl : SettingsRepositoryInMemoryImpl() {

    override val defaultSettings: MutableMap<String, Setting>
        get() = createSettings(
            "Test setting" to false,
            "Another test" to true,
            "Only current month" to true
        )
}