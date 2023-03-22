package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.lifecycle.ViewModel
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Category
import kotlinx.coroutines.flow.MutableStateFlow

class AddSpendingViewModel : ViewModel() {
    private var _amount: MutableStateFlow<Amount>? = null
    private var _category: MutableStateFlow<Category>? = null

    fun confirm() {

    }
}