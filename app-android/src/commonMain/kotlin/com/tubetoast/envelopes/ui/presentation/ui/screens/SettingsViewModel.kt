package com.tubetoast.envelopes.ui.presentation.ui.screens

import androidx.lifecycle.ViewModel
import com.tubetoast.envelopes.common.settings.MutableSettingsRepository
import com.tubetoast.envelopes.ui.presentation.ui.views.SettingItemModel
import com.tubetoast.envelopes.ui.presentation.ui.views.toItemModel
import com.tubetoast.envelopes.ui.presentation.ui.views.toSetting

class SettingsViewModel(
    private val settingsRepository: MutableSettingsRepository
) : ViewModel() {
    val items: List<SettingItemModel> =
        settingsRepository.settings.map { it.toItemModel() }

    fun toggle(
        item: SettingItemModel,
        isChecked: Boolean
    ) {
        item.checked.value = isChecked
        settingsRepository.saveChanges(item.toSetting())
    }
}
