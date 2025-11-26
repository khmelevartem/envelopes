package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.data.SpendingRepositoryInMemoryImpl
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.DateRange
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Spending
import com.tubetoast.envelopes.common.domain.models.id
import com.tubetoast.envelopes.common.domain.models.monthAsRange
import com.tubetoast.envelopes.common.domain.models.nextMonth
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class AverageCalculatorTest {

    private val spending = arrayOf(
        Spending(Amount(1), range1.start),
        Spending(Amount(1), range2.start),
        Spending(Amount(2), range3.start),
        Spending(Amount(3), range4.start)
    )

    private val snapshotsInteractor: SnapshotsInteractor = mockk {
        every { allSnapshots } returns setOf(
            snapshotOf(*spending)
        )
    }
    private val repository = SpendingRepositoryInMemoryImpl().apply {
        spending.forEach {
            add(it.id(), it)
        }
    }
    private val spendingInteractor: SpendingInteractor = SpendingInteractorImpl(repository)
    private val spendingCalculator = SpendingCalculator(snapshotsInteractor, spendingInteractor)
    private val calculator = AverageCalculator(spendingCalculator)

    @ParameterizedTest
    @MethodSource("provideData")
    fun testMoving() = runBlocking {
        val map = calculator.calculateMovingAverage(months = 2, today = range4.endInclusive)
        Assertions.assertEquals(
            mapOf(
                range2 to Amount(2),
                range3 to Amount(3),
                range4 to Amount(5)
            ),
            map
        )
    }

    private fun snapshotOf(vararg spending: Spending): EnvelopeSnapshot {
        return EnvelopeSnapshot(
            Envelope("name", Amount.ZERO),
            setOf(
                CategorySnapshot(
                    Category("category"),
                    spending.toSet()
                )
            )
        )
    }

    companion object {
        private val range1 = Date(1, 1, 1).monthAsRange()
        private val range2 = range1.nextMonth()
        private val range3 = range2.nextMonth()
        private val range4 = range3.nextMonth()

        @JvmStatic
        fun provideData(): Array<Arguments> =
            arrayOf(
                Arguments.of(
                    1,
                    mapOf(
                        range1 to Amount(1),
                        range2 to Amount(1),
                        range3 to Amount(2),
                        range4 to Amount(3)
                    )
                ),
                Arguments.of(
                    2,
                    mapOf(
                        range2 to Amount(2),
                        range3 to Amount(3),
                        range4 to Amount(5)
                    )
                ),
                Arguments.of(
                    3,
                    mapOf(
                        range3 to Amount(4),
                        range4 to Amount(6)
                    )
                ),
                Arguments.of(
                    4,
                    mapOf(
                        range4 to Amount(7)
                    )
                ),
                Arguments.of(
                    5,
                    mapOf<DateRange, Amount>()
                ),
                Arguments.of(
                    6,
                    mapOf<DateRange, Amount>()
                )
            )
    }
}
