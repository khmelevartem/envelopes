package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
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
    onAddClick: (Envelope) -> Unit,
    modifier: Modifier = Modifier,
) = Surface(color = itemModel.color, modifier = modifier) {
    Column {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            itemModel.snapshot.run {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = envelope.name)
                    Text(text = "limit: ${envelope.limit.units}")
                    Text(text = "used on $percentage%")
                }
            }
            Row {
                IconButton(onClick = { onEditClick(itemModel.snapshot.envelope) }) {
                    Icon(Icons.Rounded.Edit, contentDescription = "edit envelope")
                }
                IconButton(onClick = { onDeleteClick(itemModel.snapshot.envelope) }) {
                    Icon(Icons.Rounded.Delete, contentDescription = "delete envelope")
                }
            }
        }
        IconButton(onClick = { onAddClick(itemModel.snapshot.envelope) }) {
            Icon(Icons.Rounded.Add, contentDescription = "add category")
        }
    }
}

data class EnvelopeItemModel(
    val snapshot: EnvelopeSnapshot,
    var color: Color,
)
