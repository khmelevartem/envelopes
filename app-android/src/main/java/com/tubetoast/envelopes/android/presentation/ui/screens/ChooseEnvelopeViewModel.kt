package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tubetoast.envelopes.common.domain.CategoryInteractor
import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ChooseEnvelopeViewModel(
    private val categoryInteractor: CategoryInteractor,
    private val snapshotsInteractor: SnapshotsInteractor
) : ViewModel() {
    private val category = mutableStateOf(Category.EMPTY)
    private var chosenEnvelope = mutableStateOf(Envelope.EMPTY)

    fun envelopes(chosenEnvelopeId: String?): Flow<List<Envelope>> =
        snapshotsInteractor.envelopeSnapshotFlow
            .map {
                it.map { snapshot ->
                    snapshot.envelope.also { envelope ->
                        if (envelope.id == chosenEnvelopeId) chosenEnvelope.value = envelope
                    }
                }
            }

    fun category(id: String?): State<Category> {
        id?.let {
            viewModelScope.launch {
                categoryInteractor.getCategory(id)?.let {
                    category.value = it
                } ?: throw IllegalStateException("Trying to set category id $id that doesn't exit")
            }
        }
        return category
    }

    fun isChosen(envelope: Envelope): Boolean {
        return chosenEnvelope.value == envelope
    }

    fun setNewChosenEnvelope(envelope: Envelope) {
        viewModelScope.launch {
            categoryInteractor.moveCategory(category.value, envelope.id)
        }
        chosenEnvelope.value = envelope // crutch
    }
}
