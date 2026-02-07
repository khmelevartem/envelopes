package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.SelectableEnvelope
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot

class SelectedEnvelopesRepository(
    envelopeInteractor: EnvelopeInteractor
) : SelectedItemRepository<Envelope, SelectableEnvelope>(
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
