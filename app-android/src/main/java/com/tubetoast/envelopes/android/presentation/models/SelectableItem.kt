package com.tubetoast.envelopes.android.presentation.models

import com.tubetoast.envelopes.common.domain.models.ImmutableModel

interface SelectableItem<I : ImmutableModel<I>> {
    val item: I
    val isSelected: Boolean
}
