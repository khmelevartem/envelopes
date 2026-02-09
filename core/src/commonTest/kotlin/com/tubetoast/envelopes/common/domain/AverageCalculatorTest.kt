package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.data.SpendingInMemoryRepository
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Spending
import com.tubetoast.envelopes.common.domain.models.id
import com.tubetoast.envelopes.common.domain.models.monthAsRange
import com.tubetoast.envelopes.common.domain.models.nextMonth
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking

class AverageCalculatorTest :
    FunSpec({

        val range1 = Date(1, 1, 1).monthAsRange()
        val range2 = range1.nextMonth()
        val range3 = range2.nextMonth()
        val range4 = range3.nextMonth()

        val spendingData = arrayOf(
            Spending(Amount(1), range1.start),
            Spending(Amount(1), range2.start),
            Spending(Amount(2), range3.start),
            Spending(Amount(3), range4.start)
        )

        fun snapshotOf(vararg spending: Spending) =
            EnvelopeSnapshot(
                Envelope("name", Amount.ZERO),
                setOf(CategorySnapshot(Category("category"), spending.toSet()))
            )

        val snapshotsInteractor = FakeSnapshotsInteractor(setOf(snapshotOf(*spendingData)))
        val repository = SpendingInMemoryRepository().apply {
            spendingData.forEach {
                runBlocking {
                    add(it.id(), it)
                }
            }
        }
        val spendingInteractor = SpendingInteractorImpl(repository)
        val spendingCalculator = SpendingCalculator(snapshotsInteractor, spendingInteractor)
        val calculator = AverageCalculator(spendingCalculator)

        context("testMovingAverage") {
            withData(
                nameFn = { "months: ${it.first}" },
                1 to mapOf(range1 to Amount(1), range2 to Amount(1), range3 to Amount(2), range4 to Amount(3)),
                2 to mapOf(range2 to Amount(2), range3 to Amount(3), range4 to Amount(5)),
                3 to mapOf(range3 to Amount(4), range4 to Amount(6)),
                4 to mapOf(range4 to Amount(7)),
                5 to emptyMap(),
                6 to emptyMap()
            ) { (months, expected) ->
                calculator.calculateMovingAverage(months, today = range4.endInclusive) shouldBe expected
            }
        }
    })
