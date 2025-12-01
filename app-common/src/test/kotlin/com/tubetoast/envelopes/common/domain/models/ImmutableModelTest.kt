package com.tubetoast.envelopes.common.domain.models

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ImmutableModelTest {

    @Test
    fun `id is unique for any equal model`() {
        val ids = Array(1000) {
            TestImmutableModel(field2 = "no")
        }
        ids.map {
            it.id.code
        }.toSet().let {
            println(it)
            Assertions.assertEquals(1, it.size)
        }
    }

    @Test
    fun `id is unique for category`() {
        val ids = Array(100000) {
            Category("category for test", Amount(123))
        }
        ids.map {
            it.id.code
        }.toSet().let {
            println(it)
            Assertions.assertEquals(1, it.size)
        }
    }
}

data class TestImmutableModel(
    val field1: String = "field1",
    val field2: String = "field2",
    val field3: String = "field3",
    val field4: Int = 4,
    val field5: Int = 5,
    val field6: Int? = null,
    val field7: Boolean? = false
) : ImmutableModel<TestImmutableModel>()
