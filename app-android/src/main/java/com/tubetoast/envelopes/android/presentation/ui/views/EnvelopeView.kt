package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot

@Composable
fun EnvelopeView(snapshot: EnvelopeSnapshot) {
    Surface(color = Color.Gray) {
        Column {
            Text(text = snapshot.envelope.name)
            Text(text = snapshot.percentage.toString())
            LazyColumn() {
                items(snapshot.categories.toList()) { category ->
                    CategoryView(snapshot = category)
                }
            }
        }
    }
}