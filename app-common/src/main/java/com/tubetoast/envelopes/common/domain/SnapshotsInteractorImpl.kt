package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Hash
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SnapshotsInteractorImpl(
    private val spendingRepository: SpendingRepository,
    private val categoryRepository: CategoryRepository,
    private val envelopesRepository: EnvelopesRepository,
) : SnapshotsInteractor {

    private val flow = MutableStateFlow(envelopeSnapshot)

    init {
        listOf(spendingRepository, categoryRepository, envelopesRepository).forEach {
            it.listener = {
                flow.value = envelopeSnapshot
            }
        }
    }

    override val envelopeSnapshot: Set<EnvelopeSnapshot>
        get() = envelopesRepository.get(Hash.any()).mapTo(mutableSetOf()) { envelope ->
            EnvelopeSnapshot(
                envelope,
                categoryRepository.get(envelope.hash).mapTo(mutableSetOf()) { category ->
                    CategorySnapshot(
                        category,
                        spendingRepository.get(category.hash),
                    )
                },
            )
        }

    override val envelopeSnapshotFlow: StateFlow<Set<EnvelopeSnapshot>> = flow.asStateFlow()
}
