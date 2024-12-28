package com.tubetoast.envelopes.android.presentation.ui.screens

import com.tubetoast.envelopes.android.presentation.models.ChoosableEnvelope
import com.tubetoast.envelopes.common.domain.EnvelopeInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SelectedEnvelopesRepositoryImpl(
    envelopeInteractor: EnvelopeInteractor
) : SelectedEnvelopesRepository {

    private val _selectedEnvelopes: MutableStateFlow<Set<ChoosableEnvelope>> = MutableStateFlow(
        emptySet()
    )

    private val scope = CoroutineScope(Job())

    init {
        scope.launch {
            _selectedEnvelopes.value = envelopeInteractor.getAll().map {
                ChoosableEnvelope(it, true)
            }.toSet()
        }
    }

    override val selectedEnvelopes: StateFlow<Set<ChoosableEnvelope>>
        get() = _selectedEnvelopes.asStateFlow()

    override fun changeSelection(change: Set<ChoosableEnvelope>.() -> Set<ChoosableEnvelope>) {
        _selectedEnvelopes.value = _selectedEnvelopes.value.change()
    }
}
