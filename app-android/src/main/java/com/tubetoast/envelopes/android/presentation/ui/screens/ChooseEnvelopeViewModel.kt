package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.tubetoast.envelopes.common.domain.CategoryInteractor
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.id
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChooseEnvelopeViewModel(
    private val categoryInteractor: CategoryInteractor,
    private val envelopeInteractor: EnvelopeInteractor,
    private val snapshotsInteractor: SnapshotsInteractor
) : ViewModel() {
    private val category = mutableStateOf(Category.EMPTY)
    private var envelope = Envelope.EMPTY

    fun envelopes(): Flow<List<Envelope>> = snapshotsInteractor.envelopeSnapshotFlow
        .map { it.map(EnvelopeSnapshot::envelope) }

    fun category(id: Int?): State<Category> {
        id?.let {
            categoryInteractor.getCategory(id.id())?.let {
                category.value = it
            } ?: throw IllegalStateException("Trying to set category id $id that doesn't exit")
        }
        return category
    }

    fun setChosenEnvelope(id: Int?) {
        id?.let {
            envelopeInteractor.getExactEnvelope(id.id())?.let {
                envelope = it
            } ?: throw IllegalStateException("Trying to set envelope id $id that doesn't exit")
        }
    }

    fun isChosen(envelope: Envelope): Boolean {
        return this.envelope == envelope
    }


}