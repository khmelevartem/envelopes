package com.tubetoast.envelopes.monefy.data

import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

// TODO refactor
class SnapshotsInteractorMonefyImpl(
    lines: List<String>,
    monefyDataParser: MonefyDataParser = MonefyDataParser(),
) : SnapshotsInteractor {
    override val envelopeSnapshot: Set<EnvelopeSnapshot> = monefyDataParser.parse(lines).let {
        setOf(
            EnvelopeSnapshot(
                envelope = Envelope(
                    name = "all",
                    limit = Amount(5_000_000),
                ),
                categories = it.toSet(),

            ),
        )
    }
    override val envelopeSnapshotFlow: Flow<Set<EnvelopeSnapshot>> = flowOf(envelopeSnapshot)
}
