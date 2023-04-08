package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.common.domain.EnvelopesInteractor
import com.tubetoast.envelopes.common.domain.models.*
import com.tubetoast.envelopes.common.util.DateGenerator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddSpendingViewModel(
    private val envelopesInteractor: EnvelopesInteractor,
    private val dateGenerator: DateGenerator
) : ViewModel() {

    private val amount: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val category: MutableStateFlow<Category?> = MutableStateFlow(null)
    private val date: MutableStateFlow<Date?> = MutableStateFlow(null)
    private val comment: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState.Default)
    val uiState = _uiState.asStateFlow()
    val envelopes = envelopesInteractor.envelopeSnapshot

    fun setAmount(newAmount: Int) {
        amount.value = newAmount
    }

    fun setCategory(newCategory: Category) {
        category.value = newCategory
    }

    fun confirm() {
        val amountToConfirm = amount.value
            ?: return showWarning(reason = UIState.Warning.Reason.AMOUNT)
        val categoryToConfirm = category.value
            ?: return showWarning(reason = UIState.Warning.Reason.CATEGORY)
        val dateToConfirm = date.value
            ?: dateGenerator.now()

        try {
            envelopesInteractor.addSpending(
                Spending(
                    amount = Amount(
                        units = amountToConfirm,
                    ),
                    date = dateToConfirm,
                    comment = comment.value
                ),
                categoryToConfirm
            )
            showConfirmation()
        } catch (e: DomainException) {
            showWarning(UIState.Warning.Reason.DOMAIN)
        }
    }

    private fun showWarning(reason: UIState.Warning.Reason) {
        viewModelScope.launch {
            _uiState.value = UIState.Warning(reason)
        }
    }

    private fun showConfirmation() {
        viewModelScope.launch {
            _uiState.value = UIState.Confirmed
            delay(2_000)
            _uiState.value = UIState.Default
        }
    }

    sealed interface UIState {
        object Default : UIState
        class Warning(val reason: Reason) : UIState {
            enum class Reason { AMOUNT, CATEGORY, DOMAIN }
        }
        object Confirmed : UIState
    }
}