package com.tubetoast.envelopes.android.domain

import com.tubetoast.envelopes.android.presentation.models.SelectableCategory
import com.tubetoast.envelopes.common.domain.CategoryInteractor
import com.tubetoast.envelopes.common.domain.models.Category

class SelectedCategoryRepository(
    categoryInteractor: CategoryInteractor
) : SelectedItemRepository<Category, SelectableCategory>(
    initial = {
        categoryInteractor.getAll().map {
            SelectableCategory(
                item = it,
                isSelected = false
            )
        }.toSet()
    }
)
