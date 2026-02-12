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
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.common.utils.formatToReadableNumber
import com.tubetoast.envelopes.ui.presentation.navigation.AppNavigation
import com.tubetoast.envelopes.ui.presentation.navigation.Navigate
import com.tubetoast.envelopes.ui.presentation.ui.screens.models.EnvelopePaceInfo
import com.tubetoast.envelopes.ui.presentation.ui.screens.models.MainSummaryInfo
import com.tubetoast.envelopes.ui.presentation.ui.screens.models.PaceStatus
import com.tubetoast.envelopes.ui.presentation.ui.theme.Dimensions
import com.tubetoast.envelopes.ui.presentation.ui.theme.EColor
import com.tubetoast.envelopes.ui.presentation.ui.theme.FontWeights
import com.tubetoast.envelopes.ui.presentation.ui.theme.Typography
import com.tubetoast.envelopes.ui.presentation.ui.views.PeriodControlViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EnvelopesListScreen(
    navigate: Navigate,
    envelopesListViewModel: EnvelopesListViewModel = koinViewModel(),
    periodControlViewModel: PeriodControlViewModel = koinViewModel()
) {
    val mainSummary by envelopesListViewModel.mainSummary.collectAsState()
    val envelopePaceInfos by envelopesListViewModel.envelopePaceInfos.collectAsState()
    val elapsedPercentage by envelopesListViewModel.elapsedPercentage.collectAsState()
    val filterByYear by envelopesListViewModel.filterByYear.collectAsState()

    Scaffold(
        containerColor = EColor.BackgroundDark,
        bottomBar = { DashboardBottomNav(navigate) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            PeriodSelectorSection(periodControlViewModel, filterByYear)

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = Dimensions.PaddingMedium),
                verticalArrangement = Arrangement.spacedBy(Dimensions.SpacingSmall)
            ) {
                item {
                    MainSummarySection(mainSummary)
                }

                items(envelopePaceInfos, key = { it.envelope.id.code }) { paceInfo ->
                    EnvelopePaceCard(
                        paceInfo = paceInfo,
                        onClick = { navigate(AppNavigation.editEnvelope(paceInfo.envelope)) }
                    )
                }

                item { Spacer(Modifier.height(Dimensions.PaddingBottomNav)) }
                // Padding for BottomNav
            }
        }
    }
}

@Composable
fun PeriodSelectorSection(
    viewModel: PeriodControlViewModel,
    filterByYear: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = Dimensions.PaddingSmall)) {
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
                fontWeight = FontWeights.Bold,
                modifier = Modifier.clickable { viewModel.changePeriodType() }
            )
            IconButton(onClick = { viewModel.nextPeriod() }) {
                Icon(Icons.Default.KeyboardArrowRight, null, tint = Color.White)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = Dimensions.PaddingMedium),
            horizontalArrangement = Arrangement.spacedBy(Dimensions.SpacingSmall)
        ) {
            listOf("Month", "Year").forEach { label ->
                val isSelected = (label == "Month") == !filterByYear
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.changePeriodType() },
                    label = { Text(label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EColor.PaceMint,
                        selectedLabelColor = Color.Black
                    )
                )
            }
        }
    }
}

@Composable
fun MainSummarySection(mainSummary: MainSummaryInfo) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = Dimensions.PaddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${mainSummary.totalSpent.units.formatToReadableNumber()} ₽",
            fontSize = Typography.DisplayLarge,
            fontWeight = FontWeights.ExtraBold,
            color = Color.White
        )
        Text(
            text = "of ${mainSummary.totalLimit.units.formatToReadableNumber()} ₽",
            color = Color.Gray,
            fontSize = Typography.DisplayMedium
        )

        Spacer(Modifier.height(Dimensions.PaddingLarge))

        PaceProgressBar(
            progress = mainSummary.spentPercentage,
            elapsedPercentage = mainSummary.elapsedPercentage,
            activeColor = mainSummary.paceColor
        )

        Spacer(Modifier.height(Dimensions.SpacingSmall))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                "Remaining: ${mainSummary.remaining.formatToReadableNumber()} ₽",
                color = Color.Gray,
                fontSize = Typography.BodyLarge
            )
            Text(
                "${(mainSummary.elapsedPercentage * 100).toInt()}% of period elapsed",
                color = Color.Gray,
                fontSize = Typography.BodyLarge
            )
        }
    }
}

@Composable
fun EnvelopePaceCard(
    paceInfo: EnvelopePaceInfo,
    onClick: () -> Unit
) {
    val paceColor by animateColorAsState(paceInfo.paceColor)

    Surface(
        color = EColor.SurfaceDark,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Column(Modifier.padding(Dimensions.PaddingMedium)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("🍕 ${paceInfo.envelope.name}", color = Color.White, fontWeight = FontWeights.Bold)
                Text(
                    "${paceInfo.sum.units.formatToReadableNumber()} / ${paceInfo.limit.units.formatToReadableNumber()}",
                    color = Color.Gray,
                    fontSize = Typography.BodyLarge
                )
            }

            Spacer(Modifier.height(Dimensions.PaddingSmall))

            PaceProgressBar(
                progress = paceInfo.percentage,
                elapsedPercentage = paceInfo.elapsedPercentage,
                activeColor = paceColor,
                height = 4.dp
            )

            Spacer(Modifier.height(Dimensions.PaddingSmall))

            StatusLabel(paceInfo, paceColor)
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
    Box(modifier = Modifier.fillMaxWidth().height(height).background(EColor.TrackBackground, CircleShape)) {
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
    paceInfo: EnvelopePaceInfo,
    color: Color
) {
    val (icon, text) = when (paceInfo.status) {
        is PaceStatus.Exceeded -> Icons.Default.Delete to "Exceeded by ${paceInfo.status.amount.formatToReadableNumber()} ₽"
        is PaceStatus.AbovePace -> Icons.Default.Warning to "Above pace"
        is PaceStatus.OnTrack -> Icons.Default.CheckCircle to "On track"
        is PaceStatus.Remaining -> Icons.Default.Check to "Remaining: ${paceInfo.status.amount.formatToReadableNumber()} ₽"
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = color, modifier = Modifier.size(Dimensions.IconSmall))
        Spacer(Modifier.width(Dimensions.SpacingXSmall))
        Text(text, color = color, fontSize = Typography.BodyMedium)
    }
}

@Composable
fun DashboardBottomNav(navigate: Navigate) {
    NavigationBar(containerColor = EColor.BackgroundDark) {
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
