package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class EnvelopesInteractorImpl(
    private val spendingRepository: SpendingRepository,
    private val categoryRepository: CategoryRepository,
    private val envelopesRepository: EnvelopesRepository
) : EnvelopesInteractor {

    private val flow = MutableStateFlow(envelopeSnapshot)

    init {
        listOf(spendingRepository, categoryRepository, envelopesRepository).forEach {
//            it.listener = flow.emit(envelopeSnapshot)
        }
    }

    override val envelopeSnapshot: Set<EnvelopeSnapshot>
        get() = envelopesRepository.get().mapTo(mutableSetOf()) { envelope ->
            EnvelopeSnapshot(
                envelope,
                categoryRepository.get(envelope.hash).mapTo(mutableSetOf()) { category ->
                    CategorySnapshot(
                        category,
                        spendingRepository.get(category.hash)
                    )
                }
            )
        }

    override val envelopeSnapshotFlow: Flow<Set<EnvelopeSnapshot>> = flow.asStateFlow()

}