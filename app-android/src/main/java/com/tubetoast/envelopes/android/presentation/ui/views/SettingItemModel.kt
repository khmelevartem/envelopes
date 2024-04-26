package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.runtime.MutableState

data class SettingItemModel(
    val text: String,
    var checked: MutableState<Boolean>
)
