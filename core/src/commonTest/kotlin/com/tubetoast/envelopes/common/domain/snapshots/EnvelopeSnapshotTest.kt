package com.tubetoast.envelopes.common.domain.snapshots

import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Spending
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class EnvelopeSnapshotTest :
    FunSpec({

        fun snapshot(limit: Long) =
            EnvelopeSnapshot(
                envelope = Envelope(name = "envelope", limit = Amount(limit)),
                categories = setOf(
                    CategorySnapshot(
                        category = Category("category"),
                        transactions = setOf(
                            Spending(Amount(units = 10), Date.today()),
                            Spending(Amount(units = 30), Date.today())
                        )
                    ),
                    CategorySnapshot(
                        category = Category("category2"),
                        transactions = setOf(
                            Spending(Amount(units = 50), Date.today()),
                            Spending(Amount(units = 30), Date.today())
                        )
                    )
                )
            )

        context("percent calculation") {
            withData(100L, 1000L, 1_000_000L, 10_000_000L, 5_000_000L, 3L) { limit ->
                val snapshot = snapshot(limit)
                snapshot.sum.units shouldBe 120L
                snapshot.percentage shouldBe (120f / limit)
            }
        }
    })
