package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.common.domain.SelectedCategoryRepository
import com.tubetoast.envelopes.common.domain.models.SelectableCategory
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.settings.Setting
import com.tubetoast.envelopes.common.settings.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectableCategoriesListViewModel(
    private val selectedEnvelopesRepository: SelectedCategoryRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val showFilter = MutableStateFlow(false)
    val displayedCategories = MutableStateFlow(emptyList<SelectableCategory>())

    init {
        viewModelScope.launch {
            merge(selectedEnvelopesRepository.items, showFilter).collect {
                displayedCategories.value = if (showFilter.value) {
                    selectedEnvelopesRepository.items.value.toList()
                } else {
                    emptyList()
                }
            }
        }
    }

    val isColorful = settingsRepository.getSettingFlow(Setting.Key.COLORFUL)

    fun chooseCategory(category: Category) {
        selectedEnvelopesRepository.changeSelection {
            map {
                if (it.item == category) it.copy(isSelected = it.isSelected.not()) else it
            }.toSet()
        }
    }

    fun toggleShowFilter() {
        showFilter.update { !it }
    }
}
