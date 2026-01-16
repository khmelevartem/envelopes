package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.data.SpendingRepositoryInMemoryImpl
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Spending
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class InflationCalculatorTest {
    private val snapshotsInteractor = mockk<SnapshotsInteractor>()
    private val calculator = InflationCalculator(
        SpendingCalculator(
            snapshotsInteractor,
            SpendingInteractorImpl(SpendingRepositoryInMemoryImpl())
        )
    )

    @ParameterizedTest(name = "{1}")
    @MethodSource("provideData")
    fun `test calculate`(snapshots: Set<EnvelopeSnapshot>, expectedInflation: Float) = run {
        Assertions.assertTrue(baseDate in baseRange)
        Assertions.assertTrue(newDate in newRange)

        snapshotsInteractor.apply {
            every { allEnvelopeSnapshots } returns snapshots
        }

        runBlocking {
            Assertions.assertEquals(
                expectedInflation,
                calculator.calculateInflation(
                    baseRange = baseRange,
                    newRange = newRange
                ) { it.envelope.name != EXCLUDED }
            )
        }
    }

    companion object {

        private const val EXCLUDED = "excluded"
        private val baseRange = Date(1, 1, 1)..Date(2, 2, 2)
        private val newRange = Date(3, 2, 2)..Date(3, 3, 3)
        private val baseDate = Date(2, 2, 1)
        private val newDate = Date(4, 4, 2)

        @JvmStatic
        fun provideData() = arrayOf(
            argumentsOf(-1f, snapshotOf("", 1 to baseDate)),
            argumentsOf(Float.POSITIVE_INFINITY, snapshotOf("", 1 to newDate)),
            argumentsOf(0f, snapshotOf("", 1 to baseDate, 1 to newDate)),
            argumentsOf(1f, snapshotOf("", 1 to baseDate, 2 to newDate)),
            argumentsOf(2f, snapshotOf("", 1 to baseDate, 2 to baseDate, 3 to newDate, 6 to newDate)),
            argumentsOf(0.5f, snapshotOf("", 1 to baseDate, 3 to baseDate, 2 to newDate, 4 to newDate)),
            argumentsOf(-0.25f, snapshotOf("", 5 to baseDate, 3 to baseDate, 2 to newDate, 4 to newDate)),

            argumentsOf(-0.6f, snapshotOf("1", 5 to baseDate), snapshotOf("2", 2 to newDate)),
            argumentsOf(-1f, snapshotOf("1", 5 to baseDate), snapshotOf(EXCLUDED, 2 to newDate, 33 to baseDate))
        )

        private fun argumentsOf(inflation: Float, vararg envelopes: EnvelopeSnapshot): Arguments {
            return Arguments.of(setOf(*envelopes), inflation)
        }

        private fun snapshotOf(name: String, vararg amounts: Pair<Int, Date>): EnvelopeSnapshot {
            return EnvelopeSnapshot(
                Envelope(name, Amount.ZERO),
                setOf(
                    CategorySnapshot(
                        Category("category"),
                        amounts.map { Spending(Amount(it.first.toLong()), it.second) }
                            .toSet()
                    )
                )
            )
        }
    }
}
