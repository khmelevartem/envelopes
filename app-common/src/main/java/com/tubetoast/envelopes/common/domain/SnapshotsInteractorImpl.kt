import com.tubetoast.envelopes.common.domain.EnvelopeSnapshotRepository
import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import com.tubetoast.envelopes.common.domain.models.DateRange
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

open class SnapshotsInteractorImpl(
    private val repository: EnvelopeSnapshotRepository // New repository or method in existing
) : SnapshotsInteractor {
    override val allEnvelopeSnapshots: Set<EnvelopeSnapshot>
        get() = allEnvelopeSnapshotsFlow.value

    override val allEnvelopeSnapshotsFlow: StateFlow<Set<EnvelopeSnapshot>> =
        repository.getSnapshotsFlow()
            .stateIn(
                scope = CoroutineScope(Dispatchers.IO),
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptySet()
            )

    override fun envelopeSnapshots(period: Flow<DateRange>): Flow<Set<EnvelopeSnapshot>> {
        return combine(allEnvelopeSnapshotsFlow, period) { set, dateRange ->
            set.map { snapshot ->
                snapshot.copy(
                    categories = snapshot.categories.map { cat ->
                        cat.copy(transactions = cat.transactions.filter { it.date in dateRange }.toSet())
                    }.toSet()
                )
            }.toSet()
        }
    }
}
