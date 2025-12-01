package com.tubetoast.envelopes.android.domain

import com.tubetoast.envelopes.android.presentation.models.SelectableEnvelope
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot

class SelectedEnvelopesRepository(
    envelopeInteractor: EnvelopeInteractor
) : SelectedItemRepository<SelectableEnvelope>(
    initial = {
        envelopeInteractor.getAll().map {
            SelectableEnvelope(
                item = it,
                isSelected = true
            )
        }.toSet()
    }
)

fun SelectedEnvelopesRepository.isChosen(snapshot: EnvelopeSnapshot): Boolean =
    items.value.find { it.item == snapshot.envelope }
        ?.isSelected ?: false
