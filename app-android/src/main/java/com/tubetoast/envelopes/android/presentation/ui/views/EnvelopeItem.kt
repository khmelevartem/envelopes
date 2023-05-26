package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot

@Composable
fun EnvelopeView(itemModel: EnvelopeItemModel, modifier: Modifier = Modifier) =
    Surface(color = itemModel.color) {
        itemModel.snapshot.run {
            Column(modifier = modifier.padding(4.dp)) {
                Text(text = envelope.name)
                Text(text = "limit: ${envelope.limit.units}")
                Text(text = "used on $percentage%")
            }
        }
    }

data class EnvelopeItemModel(
    val snapshot: EnvelopeSnapshot,
    var color: Color,
)
