package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.android.domain.SelectedCategoryRepository
import com.tubetoast.envelopes.android.presentation.models.SelectableCategory
import com.tubetoast.envelopes.common.domain.models.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

class SelectableCategoriesListViewModel(
    private val selectedEnvelopesRepository: SelectedCategoryRepository
) : ViewModel() {
    private val showFilter = MutableStateFlow(true)
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

    fun chooseCategory(category: Category) {
        selectedEnvelopesRepository.changeSelection {
            map {
                if (it.item == category) it.copy(isSelected = it.isSelected.not()) else it
            }.toSet()
        }
    }

    fun toggleShowFilter() {
        showFilter.value = !showFilter.value
    }
}
