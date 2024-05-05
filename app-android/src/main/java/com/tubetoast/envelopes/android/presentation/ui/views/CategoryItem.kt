package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot

@Composable
fun CategoryView(
    snapshot: CategorySnapshot,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clickable(onClick = onClick)
            .alpha(if (snapshot.isNotEmpty()) 1f else 0.7f)
    ) {
        Text(text = snapshot.category.name, modifier = Modifier.padding(horizontal = 8.dp))
    }
}
