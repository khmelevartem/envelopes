package com.tubetoast.envelopes.android.domain

import com.tubetoast.envelopes.android.presentation.models.SelectableCategory
import com.tubetoast.envelopes.common.domain.CategoryInteractor

class SelectedCategoryRepository(
    categoryInteractor: CategoryInteractor
) : SelectedItemRepository<SelectableCategory>(
    initial = {
        categoryInteractor.getAll().map {
            SelectableCategory(
                item = it,
                isSelected = false
            )
        }.toSet()
    }
)
