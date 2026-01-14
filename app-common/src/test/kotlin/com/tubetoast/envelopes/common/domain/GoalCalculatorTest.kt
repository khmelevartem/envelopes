package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.models.Spending
import com.tubetoast.envelopes.common.domain.models.Transaction
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import com.tubetoast.envelopes.common.domain.snapshots.GoalSnapshot
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class GoalCalculatorTest {
    @Test
    fun `test savePerMonth with no finish`() {
        val snapshot = GoalSnapshot(
            goal = Goal(name = "", target = Amount(12000)),
            categories = setOf()
        )

        Assertions.assertNull(snapshot.savePerMonth())
    }

    @Test
    fun `test savePerMonth if finish is overcome`() {
        val snapshot = GoalSnapshot(
            goal = testGoal,
            categories = setOf()
        )

        Assertions.assertNull(snapshot.savePerMonth(Date(12, 12, 12)))
    }

    @Test
    fun `test savePerMonth if no full month is left`() {
        val snapshot = GoalSnapshot(
            goal = testGoal,
            categories = setOf()
        )

        Assertions.assertNull(snapshot.savePerMonth(Date(2, 10, 10)))
    }

    @Test
    fun `test savePerMonth if sum is more than target`() {
        val snapshot = GoalSnapshot(
            goal = testGoal,
            categories = setOf(
                CategorySnapshot(
                    category = Category(""),
                    transactions = setOf(
                        Spending(Amount(13000), Date(1, 1, 1))
                    )
                )
            )
        )

        Assertions.assertNull(snapshot.savePerMonth(Date(2, 2, 2)))
    }

    @ParameterizedTest(name = "expect {2}")
    @MethodSource("provideData")
    fun `test savePerMonth`(transactions: Set<Transaction<*>>, today: Date, expected: Int) {
        val snapshot = GoalSnapshot(
            goal = testGoal,
            categories = setOf(CategorySnapshot(Category(""), transactions))
        )
        val save = snapshot.savePerMonth(today)?.units
        println(save)
        Assertions.assertEquals(expected.toLong(), save)
    }

    companion object {

        val testGoal = Goal(
            name = "",
            target = Amount(12000),
            finish = Date(10, 10, 10)
        )

        @JvmStatic
        fun provideData(): Array<Arguments> {
            return arrayOf(
                Arguments.of(
                    setOf(
                        Spending(Amount(1000), Date(1, 9, 10)),
                        Spending(Amount(1000), Date(1, 8, 10))
                    ),
                    Date(9, 9, 10),
                    10000
                ),
                Arguments.of(
                    setOf(
                        Spending(Amount(1000), Date(1, 9, 10))
                    ),
                    Date(9, 9, 10),
                    11000
                ),
                Arguments.of(
                    setOf(
                        Spending(Amount(2000), Date(1, 9, 10))
                    ),
                    Date(9, 8, 10),
                    5000
                ),
                Arguments.of(
                    setOf(
                        Spending(Amount(2000), Date(1, 9, 10)),
                        Spending(Amount(4000), Date(1, 9, 10))
                    ),
                    Date(10, 10, 9),
                    500
                ),
                Arguments.of(
                    setOf(
                        Spending(Amount(2000), Date(1, 9, 10)),
                        Spending(Amount(9000), Date(1, 9, 10))
                    ),
                    Date(10, 7, 10),
                    333
                )
            )
        }
    }
}
