package com.tubetoast.envelopes.common.domain

import com.tubetoast.envelopes.common.data.SpendingInMemoryRepository
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Date
import com.tubetoast.envelopes.common.domain.models.Spending
import com.tubetoast.envelopes.common.domain.models.id
import com.tubetoast.envelopes.common.domain.models.monthAsRange
import com.tubetoast.envelopes.common.domain.models.plusMonth
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking

class SpendingInteractorImplTest :
    FunSpec({

        val date1 = Date(1, 12, 1)
        val date2 = date1.plusMonth()
        val date3 = date2.plusMonth()
        val date4 = date3.plusMonth()

        val repository = SpendingInMemoryRepository().apply {
            runBlocking {
                add(1.id(), Spending(Amount.ZERO, date1))
                add(2.id(), Spending(Amount.ZERO, date2))
                add(3.id(), Spending(Amount.ZERO, date3))
            }
        }
        val interactor = SpendingInteractorImpl(repository)

        test("testEarliest") {
            interactor.getEarliestSpending().date shouldBe date1
        }

        test("testMonths") {
            interactor.getMonthsOfSpending(today = date4) shouldBe
                listOf(date1, date2, date3, date4).map { it.monthAsRange() }
        }
    })
