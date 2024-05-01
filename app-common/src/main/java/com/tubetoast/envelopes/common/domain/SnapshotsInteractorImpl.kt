package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.DateRange
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
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

    override val allSnapshots: Set<EnvelopeSnapshot>
        get() = envelopesRepository.getAll().mapTo(mutableSetOf()) { envelope ->
            EnvelopeSnapshot(
                envelope,
                categoriesRepository.getCollection(envelope.id)
                    .map { category ->
                        CategorySnapshot(
                            category,
                            spendingRepository.getCollection(category.id).toList()
                        )
                    }
            )
        }

    override val allSnapshotsFlow: StateFlow<Set<EnvelopeSnapshot>> by lazy {
        updateFlow()
        flow.asStateFlow()
    }

    override fun snapshotsByDatesFlow(dateRange: DateRange): Flow<Set<EnvelopeSnapshot>> {
        updateFlow()
        return flow.map { set ->
            set.mapTo(mutableSetOf()) { snapshot ->
                snapshot.copy(categories = snapshot.categories.map { categorySnapshot ->
                    categorySnapshot.copy(transactions = categorySnapshot.transactions
                        .filter { transaction ->
                            transaction.date in dateRange
                        })
                })
            }
        }
    }

    private fun updateFlow() {
        scope.launch {
            flow.emit(allSnapshots)
        }
    }
}
