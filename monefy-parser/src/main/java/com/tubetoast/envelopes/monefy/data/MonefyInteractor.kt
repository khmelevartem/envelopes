package com.tubetoast.envelopes.monefy.data

import com.tubetoast.envelopes.common.domain.UpdatingCategoriesRepository
import com.tubetoast.envelopes.common.domain.UpdatingEnvelopesRepository
import com.tubetoast.envelopes.common.domain.UpdatingSpendingRepository
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Spending
import com.tubetoast.envelopes.common.domain.models.undefinedCategoriesEnvelope
import com.tubetoast.envelopes.common.domain.put
import java.io.InputStream

class MonefyInteractor(
    private val monefyDataParser: MonefyDataParser,
    private val envelopesRepository: UpdatingEnvelopesRepository,
    private val categoriesRepository: UpdatingCategoriesRepository,
    private val spendingRepository: UpdatingSpendingRepository
) {
    @JvmOverloads
    suspend fun import(inputStream: InputStream, startFrom: Date? = null): Date? {
        val categorySnapshots = monefyDataParser.parse(inputStream.reader().readLines(), startFrom)
        var prepare = true
        categorySnapshots.onEach { snapshot ->
            val onlySpendingCategorySnapshot = snapshot.copy(
                transactions = snapshot.transactions.filterIsInstance<Spending>().toSet()
            )
            if (onlySpendingCategorySnapshot.isNotEmpty()) {
                val category = onlySpendingCategorySnapshot.category
                categoriesRepository.put(category) {
                    if (prepare) {
                        envelopesRepository.put(undefinedCategoriesEnvelope)
                        prepare = false
                    }
                    undefinedCategoriesEnvelope.id
                }
                onlySpendingCategorySnapshot.transactions
                    .map { spending -> category.id to spending as Spending }
                    .let { spendingRepository.add(*it.toTypedArray()) }
            }
        }
        return categorySnapshots.flatMap { it.transactions.map { transaction -> transaction.date } }
            .maxOrNull() ?: startFrom
    }
}
