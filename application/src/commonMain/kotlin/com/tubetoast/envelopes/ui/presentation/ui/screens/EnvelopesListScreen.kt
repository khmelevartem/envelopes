package com.tubetoast.envelopes.ui.presentation.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tubetoast.envelopes.common.domain.models.Amount
import com.tubetoast.envelopes.common.domain.models.sum
import com.tubetoast.envelopes.common.domain.snapshots.EnvelopeSnapshot
import com.tubetoast.envelopes.common.utils.formatToReadableNumber
import com.tubetoast.envelopes.ui.presentation.navigation.AppNavigation
import com.tubetoast.envelopes.ui.presentation.navigation.Navigate
import com.tubetoast.envelopes.ui.presentation.ui.views.PeriodControlViewModel
import org.koin.compose.viewmodel.koinViewModel

// Theme Colors based on Pace logic
val PaceMint = Color(0xFF00C896)
val PaceAmber = Color(0xFFFFB74D)
val PaceCoral = Color(0xFFFF5252)
val SurfaceDark = Color(0xFF2A2A36)
val BackgroundDark = Color(0xFF0F0E13)

@Composable
fun EnvelopesListScreen(
    navigate: Navigate,
    envelopesListViewModel: EnvelopesListViewModel = koinViewModel(),
    periodControlViewModel: PeriodControlViewModel = koinViewModel()
) {
    val snapshots by envelopesListViewModel.itemModels.collectAsState(initial = emptyList())
    val filterByYear by envelopesListViewModel.filterByYear.collectAsState()

    // Logic for Elapsed Time Percentage (Mocked logic - should ideally come from periodControlViewModel)
    val elapsedPercentage = 0.65f // e.g., 20th day of a 30-day month

    Scaffold(
        containerColor = BackgroundDark,
        bottomBar = { DashboardBottomNav(navigate) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            PeriodSelectorSection(periodControlViewModel)

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    MainSummarySection(
                        snapshots = snapshots,
                        filterByYear = filterByYear,
                        elapsedPercentage = elapsedPercentage
                    )
                }

                val sortedSnapshots = snapshots.sortedByDescending {
                    getPaceColor(it.percentage, elapsedPercentage).value.toLong()
                }

                items(sortedSnapshots, key = { it.envelope.id.code }) { snapshot ->
                    EnvelopePaceCard(
                        snapshot = snapshot,
                        filterByYear = filterByYear,
                        elapsedPercentage = elapsedPercentage,
                        onClick = { navigate(AppNavigation.editEnvelope(snapshot.envelope)) }
                    )
                }

                item { Spacer(Modifier.height(80.dp)) } // Padding for BottomNav
            }
        }
    }
}

@Composable
fun PeriodSelectorSection(viewModel: PeriodControlViewModel) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val text by viewModel.displayedPeriod.collectAsState("")
            IconButton(onClick = { viewModel.previousPeriod() }) {
                Icon(Icons.Default.KeyboardArrowLeft, null, tint = Color.White)
            }
            Text(
                text = text,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { viewModel.changePeriodType() }
            )
            IconButton(onClick = { viewModel.nextPeriod() }) {
                Icon(Icons.Default.KeyboardArrowRight, null, tint = Color.White)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Month", "Year").forEach { label ->
                FilterChip(
                    selected = label == "Month",
                    onClick = { viewModel.changePeriodType() },
                    label = { Text(label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PaceMint,
                        selectedLabelColor = Color.Black
                    )
                )
            }
        }
    }
}

@Composable
fun MainSummarySection(
    snapshots: Iterable<EnvelopeSnapshot>,
    filterByYear: Boolean,
    elapsedPercentage: Float
) {
    val totalSpent = snapshots.map { it.sum }.sum()
    val totalLimit = snapshots.map { if (filterByYear) it.envelope.yearLimit else it.envelope.limit }.sum()
    val spentPercentage = if (totalLimit.units > 0) totalSpent / totalLimit else 0f

    val paceColor by animateColorAsState(getPaceColor(spentPercentage, elapsedPercentage))

    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${totalSpent.units.formatToReadableNumber()} ₽",
            fontSize = 42.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Text(
            text = "of ${totalLimit.units.formatToReadableNumber()} ₽",
            color = Color.Gray,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(24.dp))

        PaceProgressBar(
            progress = spentPercentage,
            elapsedPercentage = elapsedPercentage,
            activeColor = paceColor
        )

        Spacer(Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            val remaining = (totalLimit.units - totalSpent.units).coerceAtLeast(0)
            Text("Remaining: ${remaining.formatToReadableNumber()} ₽", color = Color.Gray, fontSize = 12.sp)
            Text("${(elapsedPercentage * 100).toInt()}% of period elapsed", color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun EnvelopePaceCard(
    snapshot: EnvelopeSnapshot,
    filterByYear: Boolean,
    elapsedPercentage: Float,
    onClick: () -> Unit
) {
    val limit = if (filterByYear) snapshot.envelope.yearLimit else snapshot.envelope.limit
    val paceColor by animateColorAsState(getPaceColor(snapshot.percentage, elapsedPercentage))

    Surface(
        color = SurfaceDark,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("🍕 ${snapshot.envelope.name}", color = Color.White, fontWeight = FontWeight.Bold)
                Text(
                    "${snapshot.sum.units.formatToReadableNumber()} / ${limit.units.formatToReadableNumber()}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.height(10.dp))

            PaceProgressBar(
                progress = snapshot.percentage,
                elapsedPercentage = elapsedPercentage,
                activeColor = paceColor,
                height = 4.dp
            )

            Spacer(Modifier.height(10.dp))

            StatusLabel(snapshot, limit, elapsedPercentage, paceColor)
        }
    }
}

@Composable
fun PaceProgressBar(
    progress: Float,
    elapsedPercentage: Float,
    activeColor: Color,
    height: androidx.compose.ui.unit.Dp = 8.dp
) {
    Box(modifier = Modifier.fillMaxWidth().height(height).background(Color(0xFF1C1C24), CircleShape)) {
        // Active Fill
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .fillMaxHeight()
                .background(activeColor, CircleShape)
        )

        // Elapsed Tick
        Box(
            modifier = Modifier
                .fillMaxWidth(elapsedPercentage)
                .fillMaxHeight()
                .wrapContentWidth(Alignment.End)
                .width(2.dp)
                .background(Color.White.copy(alpha = 0.6f))
        )
    }
}

@Composable
fun StatusLabel(
    snapshot: EnvelopeSnapshot,
    limit: Amount,
    elapsed: Float,
    color: Color
) {
    val remaining = limit.units - snapshot.sum.units
    val (icon, text) = when {
        snapshot.percentage > 1.0f -> Icons.Default.Delete to "Exceeded by ${(-remaining).formatToReadableNumber()} ₽"
        snapshot.percentage > elapsed + 0.1f -> Icons.Default.Warning to "Above pace"
        snapshot.percentage >= elapsed - 0.05f -> Icons.Default.CheckCircle to "On track"
        else -> Icons.Default.Check to "Remaining: ${remaining.formatToReadableNumber()} ₽"
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = color, modifier = Modifier.size(14.dp))
        Spacer(Modifier.width(4.dp))
        Text(text, color = color, fontSize = 11.sp)
    }
}

@Composable
fun DashboardBottomNav(navigate: Navigate) {
    NavigationBar(containerColor = BackgroundDark) {
        NavigationBarItem(
            selected = true,
            onClick = { navigate(AppNavigation.envelopesList()) },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navigate(AppNavigation.goalsList()) },
            icon = { Icon(Icons.Default.ShoppingCart, null) },
            label = { Text("Budget") }
        )
        Box(Modifier.weight(1f))
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Star, null) },
            label = { Text("Trends") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navigate(AppNavigation.settings()) },
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Profile") }
        )
    }
}

private fun getPaceColor(
    spendingPercentage: Float,
    elapsedPercentage: Float
): Color =
    when {
        spendingPercentage > 1.0f || spendingPercentage > elapsedPercentage + 0.15f -> PaceCoral
        spendingPercentage > elapsedPercentage -> PaceAmber
        else -> PaceMint
    }
