package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.data.SpendingRepositoryInMemoryImpl
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Spending
import com.tubetoast.envelopes.common.domain.models.id
import com.tubetoast.envelopes.common.domain.models.monthAsRange
import com.tubetoast.envelopes.common.domain.models.plusMonth
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SpendingInteractorImplTest {

    private val date1 = Date(1, 12, 1)
    private val date2 = date1.plusMonth()
    private val date3 = date2.plusMonth()
    private val date4 = date3.plusMonth()

    private val repository = SpendingRepositoryInMemoryImpl().apply {
        add(1.id(), Spending(Amount.ZERO, date1))
        add(2.id(), Spending(Amount.ZERO, date2))
        add(3.id(), Spending(Amount.ZERO, date3))
    }
    private val interactor = SpendingInteractorImpl(repository)


    @Test
    fun testEarliest() = runBlocking {
        val spending = interactor.getEarliestSpending()
        Assertions.assertEquals(date1, spending.date)
    }

    @Test
    fun testMonths() = runBlocking {
        val months = interactor.getMonthsOfSpending(today = date4)
        Assertions.assertEquals(
            listOf(date1, date2, date3, date4).map { it.monthAsRange() },
            months
        )
    }
}
