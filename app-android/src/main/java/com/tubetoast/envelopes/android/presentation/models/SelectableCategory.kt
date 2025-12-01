package com.tubetoast.envelopes.android.presentation.models

import com.tubetoast.envelopes.common.domain.models.Category

data class SelectableCategory(
    override val item: Category,
    override val isSelected: Boolean
) : SelectableItem<Category>
