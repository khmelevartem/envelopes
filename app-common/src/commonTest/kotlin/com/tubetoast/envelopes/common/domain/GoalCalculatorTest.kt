package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Goal
import com.tubetoast.envelopes.common.domain.models.Spending
import com.tubetoast.envelopes.common.domain.models.Transaction
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import com.tubetoast.envelopes.common.domain.snapshots.GoalSnapshot
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class GoalCalculatorTest :
    FunSpec({

        val testGoal = Goal(name = "", target = Amount(12000), finish = Date(10, 10, 10))

        test("savePerMonth with no finish") {
            val snapshot = GoalSnapshot(Goal(name = "", target = Amount(12000)), emptySet())
            snapshot.savePerMonth().shouldBeNull()
        }

        test("savePerMonth if finish is overcome") {
            GoalSnapshot(testGoal, emptySet()).savePerMonth(Date(12, 12, 12)).shouldBeNull()
        }

        test("savePerMonth if sum is more than target") {
            val snapshot = GoalSnapshot(
                testGoal,
                setOf(
                    CategorySnapshot(
                        Category(""),
                        setOf(
                            Spending(
                                Amount(13000),
                                Date(1, 1, 1)
                            )
                        )
                    )
                )
            )
            snapshot.savePerMonth(Date(2, 2, 2)).shouldBeNull()
        }

        context("test savePerMonth valid cases") {
            data class GoalData(
                val tx: Set<Transaction<*>>,
                val today: Date,
                val expected: Long
            )
            withData(
                GoalData(
                    setOf(
                        Spending(Amount(1000), Date(1, 9, 10)),
                        Spending(
                            Amount(1000),
                            Date(1, 8, 10)
                        )
                    ),
                    Date(9, 9, 10),
                    10000
                ),
                GoalData(
                    setOf(Spending(Amount(1000), Date(1, 9, 10))),
                    Date(9, 9, 10),
                    11000
                ),
                GoalData(setOf(Spending(Amount(2000), Date(1, 9, 10))), Date(9, 8, 10), 5000),
                GoalData(
                    setOf(
                        Spending(Amount(2000), Date(1, 9, 10)),
                        Spending(
                            Amount(4000),
                            Date(1, 9, 10)
                        )
                    ),
                    Date(10, 10, 9),
                    500
                ),
                GoalData(
                    setOf(
                        Spending(Amount(2000), Date(1, 9, 10)),
                        Spending(
                            Amount(9000),
                            Date(1, 9, 10)
                        )
                    ),
                    Date(10, 7, 10),
                    333
                )
            ) { (tx, today, expected) ->
                val snapshot =
                    GoalSnapshot(testGoal, setOf(CategorySnapshot(Category(""), tx)))
                snapshot.savePerMonth(today)?.units shouldBe expected
            }
        }
    })
