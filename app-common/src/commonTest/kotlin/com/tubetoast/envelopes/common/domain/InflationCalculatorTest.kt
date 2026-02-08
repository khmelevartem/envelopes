package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.data.SpendingInMemoryRepository
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Spending
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

class InflationCalculatorTest :
    FunSpec({

        val excluded = "excluded"
        val baseRange = Date(1, 1, 1)..Date(2, 2, 2)
        val newRange = Date(3, 2, 2)..Date(3, 3, 3)
        val baseDate = Date(2, 2, 1)
        val newDate = Date(4, 4, 2)

        fun snapshotOf(
            name: String,
            vararg amounts: Pair<Int, Date>
        ) = EnvelopeSnapshot(
            Envelope(name, Amount.ZERO),
            setOf(
                CategorySnapshot(
                    Category("category"),
                    amounts.map { Spending(Amount(it.first.toLong()), it.second) }.toSet()
                )
            )
        )

        val fakeSnapshotsInteractor = FakeSnapshotsInteractor()
        val calculator =
            InflationCalculator(
                SpendingCalculator(
                    fakeSnapshotsInteractor,
                    SpendingInteractorImpl(SpendingInMemoryRepository())
                )
            )

        test("ranges verification") {
            (baseDate in baseRange).shouldBeTrue()
            (newDate in newRange).shouldBeTrue()
        }

        context("calculate inflation") {
            withData(
                nameFn = { "Expected inflation: ${it.second}" },
                setOf(snapshotOf("", 1 to baseDate)) to -1f,
                setOf(snapshotOf("", 1 to newDate)) to Float.POSITIVE_INFINITY,
                setOf(snapshotOf("", 1 to baseDate, 1 to newDate)) to 0f,
                setOf(snapshotOf("", 1 to baseDate, 2 to newDate)) to 1f,
                setOf(snapshotOf("1", 5 to baseDate), snapshotOf("2", 2 to newDate)) to -0.6f,
                setOf(snapshotOf("1", 5 to baseDate), snapshotOf(excluded, 2 to newDate, 33 to baseDate)) to -1f
            ) { (snapshots, expected) ->
                fakeSnapshotsInteractor.snapshots = snapshots
                calculator.calculateInflation(baseRange, newRange) { it.envelope.name != excluded } shouldBe expected
            }
        }
    })
