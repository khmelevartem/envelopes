package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.SelectableCategory

class SelectedCategoryRepository(
    categoryInteractor: CategoryInteractor
) : SelectedItemRepository<Category, SelectableCategory>(
        initial = {
            categoryInteractor
                .getAll()
                .map {
                    SelectableCategory(
                        item = it,
                        isSelected = false
                    )
                }.toSet()
        }
    )
