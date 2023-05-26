package com.tubetoast.envelopes.android.presentation.ui.screens

import androidx.lifecycle.ViewModel
import com.tubetoast.envelopes.android.presentation.ui.theme.EColor
import com.tubetoast.envelopes.android.presentation.ui.views.EnvelopeItemModel
import com.tubetoast.envelopes.common.domain.EditInteractor
import com.tubetoast.envelopes.common.domain.SnapshotsInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EnvelopesListViewModel(
    snapshotsInteractor: SnapshotsInteractor,
    private val editInteractor: EditInteractor
) : ViewModel() {
    val itemModels: Flow<List<EnvelopeItemModel>> = snapshotsInteractor.envelopeSnapshotFlow.map {
        it.mapIndexed { index, snapshot ->
            EnvelopeItemModel(snapshot, EColor.palette[index.mod(EColor.palette.size)])
        }
    }

    fun addEnvelope() {
        editInteractor.addEnvelope()
    }
}
