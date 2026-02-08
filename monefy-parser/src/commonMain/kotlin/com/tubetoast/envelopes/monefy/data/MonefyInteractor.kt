package com.tubetoast.envelopes.monefy.data

import com.tubetoast.envelopes.common.domain.CategoriesRepository
import com.tubetoast.envelopes.common.domain.EnvelopesRepository
import com.tubetoast.envelopes.common.domain.SpendingRepository
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Spending
import com.tubetoast.envelopes.common.domain.models.undefinedCategoriesEnvelope
import com.tubetoast.envelopes.common.domain.put

class MonefyInteractor(
    private val monefyDataParser: MonefyDataParser,
    private val envelopesRepository: EnvelopesRepository,
    private val categoriesRepository: CategoriesRepository,
    private val spendingRepository: SpendingRepository
) {
    suspend fun import(
        lines: List<String>,
        startFrom: Date? = null
    ): Date? {
        val categorySnapshots = monefyDataParser.parse(lines, startFrom)
        var prepare = true

        categorySnapshots.forEach { snapshot ->
            val onlySpendingTransactions = snapshot.transactions.filterIsInstance<Spending>().toSet()

            if (onlySpendingTransactions.isNotEmpty()) {
                val category = snapshot.category
                categoriesRepository.put(category) {
                    if (prepare) {
                        envelopesRepository.put(undefinedCategoriesEnvelope)
                        prepare = false
                    }
                    undefinedCategoriesEnvelope.id
                }

                val spendingPairs = onlySpendingTransactions.map { category.id to it }
                spendingRepository.add(*spendingPairs.toTypedArray())
            }
        }

        return categorySnapshots
            .flatMap { it.transactions.map { t -> t.date } }
            .maxOrNull() ?: startFrom
    }
}
