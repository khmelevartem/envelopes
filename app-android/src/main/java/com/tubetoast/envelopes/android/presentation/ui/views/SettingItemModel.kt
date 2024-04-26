package com.tubetoast.envelopes.android.presentation.ui.views

import com.tubetoast.envelopes.android.settings.Setting
import kotlinx.coroutines.flow.MutableStateFlow

data class SettingItemModel(
    val text: String,
    var checked: MutableStateFlow<Boolean>
)

fun SettingItemModel(text: String, checked: Boolean) =
    SettingItemModel(text, MutableStateFlow(checked))

fun SettingItemModel.toSetting() =
    Setting(text, checked.value)

fun Setting.toItemModel() =
    SettingItemModel(text, checked)

