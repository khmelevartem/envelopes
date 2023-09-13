package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SnapshotsInteractorImpl(
    private val spendingRepository: SpendingRepository,
    private val categoriesRepository: CategoriesRepository,
    private val envelopesRepository: EnvelopesRepository,
) : SnapshotsInteractor {

    private val flow = MutableStateFlow(envelopeSnapshot)

    init {
        listOf(spendingRepository, categoriesRepository, envelopesRepository).forEach {
            it.listener = {
                flow.value = envelopeSnapshot
            }
        }
    }

    override val envelopeSnapshot: Set<EnvelopeSnapshot>
        get() = envelopesRepository.getAll().mapTo(mutableSetOf()) { envelope ->
            EnvelopeSnapshot(
                envelope,
                categoriesRepository.getCollection(envelope.hash)
                    .mapTo(mutableSetOf()) { category ->
                        CategorySnapshot(
                            category,
                            spendingRepository.getCollection(category.hash),
                        )
                    },
            )
        }

    override val envelopeSnapshotFlow: StateFlow<Set<EnvelopeSnapshot>> = flow.asStateFlow()
}
