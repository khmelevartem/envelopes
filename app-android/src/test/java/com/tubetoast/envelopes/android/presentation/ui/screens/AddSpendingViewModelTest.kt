package com.tubetoast.envelopes.android.presentation.ui.screens

import com.google.common.truth.Truth.assertThat
import com.tubetoast.envelopes.android.util.JavaDateGenerator
import com.tubetoast.envelopes.common.data.EnvelopesRepositoryImpl
import com.tubetoast.envelopes.common.domain.EnvelopesInteractorImpl
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Category
import com.tubetoast.envelopes.common.domain.models.Envelope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

internal class AddSpendingViewModelTest {

    private val sum = 4
    private val limit = 20
    private val startCategory = Category(name = "Shops", spending = listOf(), limit = null)
    private val startEnvelope =
        Envelope(name = "Food", categories = setOf(startCategory), limit = Amount(limit))
    private val interactor = EnvelopesInteractorImpl(EnvelopesRepositoryImpl())
    private val viewModel = AddSpendingViewModel(interactor, JavaDateGenerator())

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        interactor.addEnvelope(startEnvelope)
    }

    @Test
    fun testAddSpending() {
        viewModel.apply {
            repeat(2) {
                envelopes.value.forEach {
                    setAmount(sum)
                    setCategory(it.categories.first())
                    confirm()
                }
            }
        }
        val envelope = viewModel.envelopes.value.find { it.name == startEnvelope.name }
            ?: throw IllegalStateException()
        assertThat(envelope.percentage).isEqualTo(0.4f)
        val category = envelope.categories.find { it.name == startCategory.name }
            ?: throw IllegalStateException()
        assertThat(category.spending).isNotEmpty()
    }
}