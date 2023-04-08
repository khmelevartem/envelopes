package com.tubetoast.envelopes.common.data

import com.tubetoast.envelopes.common.domain.EnvelopesRepository
import com.tubetoast.envelopes.common.domain.models.*
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.flow.StateFlow

class EnvelopesRepositoryImpl(
    private val envelopesSource: EnvelopesSource,
) : EnvelopesRepository() {
    override fun addImpl(value: Envelope, keyHash: Hash<Any>): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteImpl(valueHash: Hash<Envelope>): Boolean {
        TODO("Not yet implemented")
    }

    override fun editImpl(valueHash: Hash<Envelope>, newM: Envelope): Boolean {
        TODO("Not yet implemented")
    }

    override fun get(keyHash: Hash<Any>): Set<Envelope> {
        TODO("Not yet implemented")
    }


}