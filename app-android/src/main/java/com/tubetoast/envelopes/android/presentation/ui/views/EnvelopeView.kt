package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot

@Composable
fun EnvelopeView(snapshot: EnvelopeSnapshot, modifier: Modifier = Modifier) =
    snapshot.run {
        Column(modifier = modifier.padding(PaddingValues(8.dp))) {
            Text(text = envelope.name)
            Text(text = "limit: ${envelope.limit}")
            Text(text = "used on $percentage%")
        }
    }
