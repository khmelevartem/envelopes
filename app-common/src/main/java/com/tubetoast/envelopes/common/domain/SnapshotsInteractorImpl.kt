package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.DateRange
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class SnapshotsInteractorImpl(
    private val spendingRepository: UpdatingSpendingRepository,
    private val categoriesRepository: UpdatingCategoriesRepository,
    private val envelopesRepository: UpdatingEnvelopesRepository
) : SnapshotsInteractor {

    private val flow = MutableStateFlow<Set<EnvelopeSnapshot>>(emptySet())
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        listOf(spendingRepository, categoriesRepository, envelopesRepository).forEach {
            it.addUpdateListener(::updateFlow)
        }
        envelopesRepository.addDeleteListener(categoriesRepository::onKeyDelete)
        categoriesRepository.addDeleteListener(spendingRepository::onKeyDelete)
    }

    override val allEnvelopeSnapshots: Set<EnvelopeSnapshot>
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

    override val allEnvelopeSnapshotsFlow: StateFlow<Set<EnvelopeSnapshot>> by lazy {
        updateFlow()
        flow.asStateFlow()
    }

    override fun envelopeSnapshots(period: Flow<DateRange>): Flow<Set<EnvelopeSnapshot>> {
        updateFlow()
        return combine(flow, period) { set, dateRange ->
            set.mapTo(mutableSetOf()) { snapshot ->
                snapshot.copy(
                    categories = snapshot.categories.mapTo(mutableSetOf()) { categorySnapshot ->
                        categorySnapshot.copy(
                            transactions = categorySnapshot.transactions
                                .filterTo(mutableSetOf()) { transaction ->
                                    transaction.date in dateRange
                                }
                        )
                    }
                )
            }
        }
    }

    private fun updateFlow() {
        scope.launch {
            flow.update { allEnvelopeSnapshots }
        }
    }
}
