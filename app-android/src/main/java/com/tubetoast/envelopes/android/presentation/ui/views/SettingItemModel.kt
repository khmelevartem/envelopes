package com.tubetoast.envelopes.android.presentation.ui.views

import com.tubetoast.envelopes.android.settings.Setting
import kotlinx.coroutines.flow.MutableStateFlow

data class SettingItemModel(
    val key: Setting.Key,
    val text: String,
    var checked: MutableStateFlow<Boolean>
)

fun SettingItemModel(key: Setting.Key, text: String, checked: Boolean) =
    SettingItemModel(key, text, MutableStateFlow(checked))

fun SettingItemModel.toSetting() =
    Setting(key, text, checked.value)

fun Setting.toItemModel() =
    SettingItemModel(key, text, checked)

