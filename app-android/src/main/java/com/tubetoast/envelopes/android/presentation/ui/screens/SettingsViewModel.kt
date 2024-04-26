package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.tubetoast.envelopes.android.presentation.ui.views.SettingItemModel

class SettingsViewModel : ViewModel() {

    val items: State<List<SettingItemModel>> = mutableStateOf(
        listOf(
            SettingItemModel(
                text = "Test setting", checked = mutableStateOf(false)
            )
        )
    )

    fun toggle(item: SettingItemModel, isChecked: Boolean) {
        item.checked.value = isChecked
    }
}