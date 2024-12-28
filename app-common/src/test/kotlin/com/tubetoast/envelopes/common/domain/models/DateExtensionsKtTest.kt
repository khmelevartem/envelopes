package com.tubetoast.envelopes.common.domain.models

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class DateExtensionsKtTest {

    @ParameterizedTest
    @MethodSource("provideDataForTestDaysOfYear")
    fun testDaysOfYear(day: Int, date: Date) {
        Assertions.assertEquals(day, date.inDaysOfYear())
    }

    @ParameterizedTest(name = "{2} from {0} to {1}")
    @MethodSource("provideDataForTestInMonths")
    fun testInMonths(start: Date, end: Date, months: Int) {
        Assertions.assertEquals(months, (start..end).inMonths())
    }

    companion object {

        @JvmStatic
        fun provideDataForTestDaysOfYear(): Array<Arguments> {
            return arrayOf(
                Arguments.of(1, Date(1, 1, 1)),
                Arguments.of(2, Date(2, 1, 145)),
                Arguments.of(32, Date(1, 2, 33)),
                Arguments.of(33, Date(2, 2, 2)),
                Arguments.of(60, Date(1, 3, 1)),
                Arguments.of(61, Date(1, 3, 4)),
                Arguments.of(365, Date(31, 12, 5)),
                Arguments.of(366, Date(31, 12, 44))
            )
        }

        @JvmStatic
        fun provideDataForTestInMonths(): Array<Arguments> {
            return arrayOf(

                Arguments.of(Date(1, 1, 1), Date(2, 1, 1), 0),
                Arguments.of(Date(1, 1, 1), Date(1, 2, 1), 1),
                Arguments.of(Date(2, 1, 1), Date(1, 2, 1), 1), // round
                Arguments.of(Date(2, 1, 1), Date(1, 3, 1), 1),
                Arguments.of(Date(30, 1, 1), Date(1, 3, 1), 1),

                Arguments.of(Date(1, 1, 1), Date(1, 1, 2), 12),
                Arguments.of(Date(1, 1, 1), Date(2, 1, 2), 12),
                Arguments.of(Date(2, 1, 1), Date(1, 2, 2), 13), // round
                Arguments.of(Date(1, 1, 1), Date(2, 2, 2), 13),

                Arguments.of(Date(1, 12, 1), Date(1, 1, 2), 1),
                Arguments.of(Date(31, 12, 1), Date(1, 1, 2), 0),

                Arguments.of(Date(1, 1, 2019), Date(28, 12, 2024), 72), // round
                Arguments.of(Date(1, 1, 2019), Date(1, 1, 2021), 24),
                Arguments.of(Date(1, 1, 2019), Date(1, 1, 2022), 36)
            )
        }
    }
}
