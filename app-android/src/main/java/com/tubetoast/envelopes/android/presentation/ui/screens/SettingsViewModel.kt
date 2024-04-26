package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.lifecycle.ViewModel
import com.tubetoast.envelopes.android.presentation.ui.views.SettingItemModel
import com.tubetoast.envelopes.android.presentation.ui.views.toItemModel
import com.tubetoast.envelopes.android.presentation.ui.views.toSetting
import com.tubetoast.envelopes.android.settings.SettingsRepository

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val items: List<SettingItemModel> =
        settingsRepository.settings.map { it.toItemModel() }

    fun toggle(item: SettingItemModel, isChecked: Boolean) {
        item.checked.value = isChecked
        settingsRepository.saveChanges(listOf(item.toSetting()))
    }
}