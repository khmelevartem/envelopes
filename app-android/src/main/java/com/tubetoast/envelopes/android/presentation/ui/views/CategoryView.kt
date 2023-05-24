package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot

@Composable
fun CategoryView(snapshot: CategorySnapshot) {
    Surface(color = Color.Green) {
        snapshot.category.let {
            Text(text = "${it.name} with ${it.limit?.units?.toString()}")
        }
    }
}