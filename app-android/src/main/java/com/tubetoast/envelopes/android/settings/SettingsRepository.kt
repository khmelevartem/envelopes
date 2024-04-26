package com.tubetoast.envelopes.android.settings

interface SettingsRepository {
    val settings: List<Setting>
    fun saveChanges(settings: List<Setting>)
}