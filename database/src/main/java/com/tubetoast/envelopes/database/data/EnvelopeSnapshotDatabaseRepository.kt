package com.tubetoast.envelopes.database.data

import com.tubetoast.envelopes.common.domain.EnvelopeSnapshotRepository
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import com.tubetoast.envelopes.database.data.dao.EnvelopeSnapshotDao
import com.tubetoast.envelopes.database.data.relations.CategoryWithSpending
import com.tubetoast.envelopes.database.data.relations.EnvelopeWithCategories
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EnvelopeSnapshotDatabaseRepository(
    private val dao: EnvelopeSnapshotDao,
    private val envelopeConverter: EnvelopeConverter,
    private val categoryConverter: CategoryConverter,
    private val spendingConverter: SpendingConverter
) : EnvelopeSnapshotRepository {

    override fun getSnapshotsFlow(): Flow<Set<EnvelopeSnapshot>> {
        return dao.getFullSnapshotsFlow().map { entities ->
            entities.map { it.toSnapshot() }.toSet()
        }
    }

    private fun EnvelopeWithCategories.toSnapshot(): EnvelopeSnapshot {
        return EnvelopeSnapshot(
            envelope = envelopeConverter.toDomainModel(envelope),
            categories = categories.map { it.toSnapshot() }.toSet()
        )
    }

    private fun CategoryWithSpending.toSnapshot(): CategorySnapshot {
        return CategorySnapshot(
            category = categoryConverter.toDomainModel(category),
            transactions = spending.map { spendingConverter.toDomainModel(it) }.toSet()
        )
    }
}
