package com.tubetoast.envelopes.ui.settings

import com.tubetoast.envelopes.common.settings.MutableSettingsRepository
import com.tubetoast.envelopes.common.settings.Setting
import com.tubetoast.envelopes.common.settings.SettingsRepository
import com.tubetoast.envelopes.ui.presentation.LocalKVStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryKVStoreImpl(
    private val kvStore: LocalKVStore,
    private val defaultSettingsRepo: SettingsRepository
) : MutableSettingsRepository {
    private val flows = mutableMapOf<Setting.Key, MutableStateFlow<Setting>>()

    override val settings: List<Setting>
        get() = Setting.Key.entries.map { getSetting(it) }

    override fun getSetting(key: Setting.Key): Setting {
        val default = defaultSettingsRepo.getSetting(key)
        val savedIsChecked = kvStore.getBoolean(key.name, default.checked)
        return default.copy(checked = savedIsChecked)
    }

    override fun getSettingFlow(key: Setting.Key): StateFlow<Setting> =
        flows
            .getOrPut(key) {
                MutableStateFlow(getSetting(key))
            }.asStateFlow()

    override fun saveChanges(settings: List<Setting>) {
        saveChanges(*settings.toTypedArray())
    }

    override fun saveChanges(vararg settings: Setting) {
        settings.forEach {
            kvStore.putBoolean(it.key.name, it.checked)
            flows.getOrPut(it.key) { MutableStateFlow(it) }.tryEmit(it)
        }
    }
}
