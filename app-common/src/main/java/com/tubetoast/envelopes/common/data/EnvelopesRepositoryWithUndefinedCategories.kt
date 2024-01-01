package com.tubetoast.envelopes.common.data

import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.put


class EnvelopesRepositoryWithUndefinedCategories : EnvelopesRepositoryInMemoryBase() {
    init {
        put(undefinedCategoriesEnvelope)
    }
    companion object {
        val undefinedCategoriesEnvelope = Envelope(name = "Undefined", limit = Amount.ZERO)
    }
}
