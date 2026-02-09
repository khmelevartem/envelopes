package com.tubetoast.envelopes.common.domain.models

data class SelectableCategory(
    override val item: Category,
    override val isSelected: Boolean
) : SelectableItem<Category>
