package com.tubetoast.envelopes.android.presentation.models

import com.tubetoast.envelopes.common.domain.models.Envelope

data class ChoosableEnvelope(
    val envelope: Envelope,
    val isChosen: Boolean
)
