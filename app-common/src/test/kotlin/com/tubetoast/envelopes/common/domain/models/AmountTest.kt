package com.tubetoast.envelopes.common.domain.models

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class AmountTest {
    @Test
    fun testSumOf() {
        listOf(
            Amount(5),
            Amount(2),
            Amount(3),
            Amount(0),
        ).sum().let {
            assertThat(it).isEqualTo(Amount(10))
        }

    }
}