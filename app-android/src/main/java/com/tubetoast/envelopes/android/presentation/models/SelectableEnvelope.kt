package com.tubetoast.envelopes.android.presentation.models

import com.tubetoast.envelopes.common.domain.models.Envelope

data class SelectableEnvelope(
    override val item: Envelope,
    override val isSelected: Boolean
) : SelectableItem<Envelope>
