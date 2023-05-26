package com.tubetoast.envelopes.common.domain.stub

import com.tubetoast.envelopes.common.domain.EditInteractor
import com.tubetoast.envelopes.common.domain.EnvelopesRepository
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.put
import java.util.LinkedList
import java.util.Queue

class StubEditInteractorImpl(
    private val envelopesRepository: EnvelopesRepository,
) : EditInteractor {
    private val envelopes: Queue<Envelope> = LinkedList(
        listOf(
            Envelope("food", Amount(23)),
            Envelope("sport", Amount(22)),
            Envelope("car", Amount(2)),
            Envelope("gadgets", Amount(10)),
            Envelope("fun", Amount(123)),
            Envelope("cat", Amount(30)),
            Envelope("child", Amount(50)),
            Envelope("wife", Amount(50)),
        )
    )

    override fun addEnvelope() {
        envelopesRepository.put(envelopes.poll())
    }
}
