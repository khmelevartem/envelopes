package com.tubetoast.envelopes.common.domain.models

interface SelectableItem<I : ImmutableModel<I>> {
    val item: I
    val isSelected: Boolean
}
