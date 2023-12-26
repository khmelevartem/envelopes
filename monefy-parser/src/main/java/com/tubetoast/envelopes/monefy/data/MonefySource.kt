package com.tubetoast.envelopes.monefy.data

import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.io.InputStream

class MonefySource(
    input: InputStream,
    monefyDataParser: MonefyDataParser = MonefyDataParser(),
) {

    val categorySnapshots = input.reader().use { monefyDataParser.parse(it.readLines()).toSet() }

}
