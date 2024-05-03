package com.tubetoast.envelopes.common.domain.snapshots

import com.google.common.truth.Truth.assertThat
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.models.Spending
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class EnvelopeSnapshotTest {
    private fun snapshot(limit: Int) = EnvelopeSnapshot(
        envelope = Envelope(name = "envelope", limit = Amount(limit)),
        categories = setOf(
            CategorySnapshot(
                category = Category("category"),
                transactions = setOf(
                    Spending(
                        amount = Amount(units = 10),
                        date = Date.today()
                    ),
                    Spending(
                        amount = Amount(units = 30),
                        date = Date.today()
                    )
                )

            ),
            CategorySnapshot(
                category = Category("category2"),
                transactions = setOf(
                    Spending(
                        amount = Amount(units = 50),
                        date = Date.today()
                    ),
                    Spending(
                        amount = Amount(units = 30),
                        date = Date.today()
                    )
                )
            )
        )
    )

    @ParameterizedTest
    @ValueSource(ints = [100, 1000, 1_000_000, 10_000_000, 5_000_000, 3])
    fun percent(limit: Int) {
        val snapshot = snapshot(limit)
        assertThat(snapshot.sum.units).isEqualTo(120)
        assertThat(snapshot.percentage).isEqualTo(120f / limit)
    }
}
