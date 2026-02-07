package com.tubetoast.envelopes.common.domain.models

data class SelectableEnvelope(
    override val item: Envelope,
    override val isSelected: Boolean
) : SelectableItem<Envelope>
