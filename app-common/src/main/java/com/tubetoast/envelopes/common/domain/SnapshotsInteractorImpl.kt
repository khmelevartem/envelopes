package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SnapshotsInteractorImpl(
    private val spendingRepository: UpdatingSpendingRepository,
    private val categoriesRepository: UpdatingCategoriesRepository,
    private val envelopesRepository: UpdatingEnvelopesRepository
) : SnapshotsInteractor {

    private val flow = MutableStateFlow<Set<EnvelopeSnapshot>>(emptySet())
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    init {
        listOf(spendingRepository, categoriesRepository, envelopesRepository).forEach {
            it.update = ::updateFlow
        }
        envelopesRepository.deleteListener = categoriesRepository.deleteListenerImpl
        categoriesRepository.deleteListener = spendingRepository.deleteListenerImpl
    }

    override val envelopeSnapshot: Set<EnvelopeSnapshot>
        get() = envelopesRepository.getAll().mapTo(mutableSetOf()) { envelope ->
            EnvelopeSnapshot(
                envelope,
                categoriesRepository.getCollection(envelope.id)
                    .mapTo(mutableSetOf()) { category ->
                        CategorySnapshot(
                            category,
                            spendingRepository.getCollection(category.id)
                        )
                    }
            )
        }

    override val envelopeSnapshotFlow: StateFlow<Set<EnvelopeSnapshot>> by lazy {
        updateFlow()
        flow.asStateFlow()
    }

    private fun updateFlow() {
        scope.launch {
            flow.emit(envelopeSnapshot)
        }
    }
}
