package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot

class SnapshotsInteractorCachingImpl(
    spendingRepository: UpdatingSpendingRepository,
    categoriesRepository: UpdatingCategoriesRepository,
    envelopesRepository: UpdatingEnvelopesRepository,
    selectedPeriodRepository: SelectedPeriodRepository
) : SnapshotsInteractorImpl(spendingRepository, categoriesRepository, envelopesRepository, selectedPeriodRepository) {

    private var isOutdated
        get() = snapshotsCache == null
        set(value) {
            if (value) snapshotsCache = null
        }

    private var snapshotsCache: Set<EnvelopeSnapshot>? = null

    @Suppress("RecursivePropertyAccessor")
    override val allSnapshots: Set<EnvelopeSnapshot>
        get() {
            if (isOutdated) {
                snapshotsCache = super.allSnapshots
            }
            return snapshotsCache ?: allSnapshots
        }

    init {
        listOf(spendingRepository, categoriesRepository, envelopesRepository).forEach {
            it.update = it.update.let { innerUpdate ->
                {
                    isOutdated = true
                    innerUpdate?.invoke()
                }
            }
        }
    }
}
