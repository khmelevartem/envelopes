package com.tubetoast.envelopes.android.settings

abstract class SettingsRepositoryInMemoryImpl : SettingsRepositoryDefaultImpl(),
    MutableSettingsRepository {

    override fun saveChanges(settings: List<Setting>) {
        settings.forEach {
            defaultSettings[it.key] = it
        }
    }
}