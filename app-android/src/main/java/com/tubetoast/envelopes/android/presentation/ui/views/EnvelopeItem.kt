package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.common.domain.models.Envelope
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot

@Composable
fun EnvelopeView(
    itemModel: EnvelopeItemModel,
    onEditClick: (Envelope) -> Unit,
    onDeleteClick: (Envelope) -> Unit,
    modifier: Modifier = Modifier,
) = Surface(color = itemModel.color) {
    Row {
        itemModel.snapshot.run {
            Column(modifier = modifier.padding(4.dp)) {
                Text(text = envelope.name)
                Text(text = "limit: ${envelope.limit.units}")
                Text(text = "used on $percentage%")
            }
        }
        Button(onClick = { onEditClick(itemModel.snapshot.envelope) }) {
            Text(text = "Edit")
        }
        Button(onClick = { onDeleteClick(itemModel.snapshot.envelope) }) {
            Text(text = "Delete")
        }
    }
}

data class EnvelopeItemModel(
    val snapshot: EnvelopeSnapshot,
    var color: Color,
)
