package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.android.presentation.ui.theme.Shapes
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot

@Composable
fun CategoryView(snapshot: CategorySnapshot, color: Color) {
    Surface(
        color = color,
        shape = Shapes.medium,
    ) {
        Text(text = snapshot.category.name, modifier = Modifier.padding(4.dp))
    }
}
