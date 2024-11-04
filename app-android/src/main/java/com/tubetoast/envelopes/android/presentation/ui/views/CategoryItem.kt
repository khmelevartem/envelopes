package com.tubetoast.envelopes.android.presentation.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.android.presentation.utils.formatToReadableNumber
import com.tubetoast.envelopes.common.domain.snapshots.CategorySnapshot
import com.tubetoast.envelopes.common.domain.snapshots.sum

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

@Composable
fun CategoryWithSumView(
    snapshot: CategorySnapshot,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .alpha(if (snapshot.isNotEmpty()) 1f else 0.7f),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = snapshot.category.name, modifier = Modifier.padding(horizontal = 8.dp))
        Text(text = snapshot.sum().formatToReadableNumber(), modifier = Modifier.padding(horizontal = 8.dp))
    }
}
