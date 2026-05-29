package com.minlish.app.presentation.screens.analytics

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minlish.app.presentation.components.AppHeader
import com.minlish.app.presentation.components.Footer
import com.minlish.app.ui.theme.*

// ═══════════════════════════════════════════════════════════════════════════════
// Root Screen — có Scaffold riêng (per-screen Scaffold pattern)
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun AnalyticsScreen(
    currentRoute: String = "Analytics",
    onNavigate: (String) -> Unit = {},
    viewModel: AnalyticsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier  = Modifier.fillMaxSize(),
        topBar    = {
            AppHeader(
                userName            = "QuangLe",
                userAvatarUrl       = "https://api.dicebear.com/7.x/avataaars/png?seed=QuangLe",
                onNotificationClick = {}
            )
        },
        bottomBar = {
            Footer(
                currentRoute = currentRoute,
                onNavigate   = onNavigate
            )
        },
        containerColor = MlBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                start  = 16.dp,
                end    = 16.dp,
                top    = 16.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { AnalyticsHeader() }
            item { StatsBentoGrid(uiState) }
            item { WordsMasteredCard(uiState) }
            item { WeeklyProgressChart(uiState) }
            item { MasteryDistributionCard(uiState) }
            item { ActivityHeatmapCard(uiState) }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// Header
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun AnalyticsHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text  = "Analytics",
            style = MaterialTheme.typography.headlineLarge,
            color = MlOnSurface
        )
        Text(
            text  = "Track your language learning journey.",
            style = MaterialTheme.typography.bodySmall,
            color = MlOnSurfaceVariant
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// Stats Bento Grid — 2 card ngang
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun StatsBentoGrid(uiState: AnalyticsUiState) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            modifier    = Modifier.weight(1f),
            icon        = Icons.Filled.LocalFireDepartment,
            iconTint    = MlWarning,
            iconBgColor = MlWarning.copy(alpha = 0.15f),
            label       = "Current Streak",
            value       = "${uiState.currentStreak}",
            unit        = "Days"
        )
        StatCard(
            modifier    = Modifier.weight(1f),
            icon        = Icons.Filled.Timer,
            iconTint    = MlInfo,
            iconBgColor = MlInfo.copy(alpha = 0.15f),
            label       = "Time Spent",
            value       = "${uiState.timeSpentHours}",
            unit        = "Hrs"
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconTint: Color,
    iconBgColor: Color,
    label: String,
    value: String,
    unit: String
) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MlSurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier  = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier         = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = icon,
                    contentDescription = null,
                    tint               = iconTint,
                    modifier           = Modifier.size(20.dp)
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text  = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MlOnSurfaceVariant
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text       = value,
                        style      = MaterialTheme.typography.headlineMedium,
                        color      = MlOnSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text     = unit,
                        style    = MaterialTheme.typography.bodySmall,
                        color    = MlOnSurfaceVariant,
                        modifier = Modifier.padding(bottom = 3.dp)
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// Words Mastered Card
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun WordsMasteredCard(uiState: AnalyticsUiState) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MlSurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Box(
                    modifier         = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MlSuccess.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = Icons.Filled.Star,
                        contentDescription = null,
                        tint               = MlSuccess,
                        modifier           = Modifier.size(24.dp)
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text  = "Words Mastered",
                        style = MaterialTheme.typography.labelMedium,
                        color = MlOnSurfaceVariant
                    )
                    Text(
                        text       = "${uiState.wordsMastered}",
                        style      = MaterialTheme.typography.headlineSmall,
                        color      = MlOnSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Surface(
                color = MlSuccess.copy(alpha = 0.12f),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text     = "+${uiState.wordsThisWeek} this week",
                    style    = MaterialTheme.typography.labelSmall,
                    color    = MlSuccess,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// Weekly Progress Chart
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun WeeklyProgressChart(uiState: AnalyticsUiState) {
    val maxMinutes = uiState.weeklyMinutes.maxOf { it.second }.coerceAtLeast(1)

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MlSurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.Bottom
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text  = "Weekly Progress",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MlOnSurface
                    )
                    Text(
                        text  = "Minutes per day",
                        style = MaterialTheme.typography.bodySmall,
                        color = MlOnSurfaceVariant
                    )
                }
                TextButton(onClick = {}) {
                    Text(
                        text  = "Details",
                        style = MaterialTheme.typography.labelMedium,
                        color = MlPrimary
                    )
                }
            }

            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                uiState.weeklyMinutes.forEach { (dayLabel, minutes) ->
                    BarChartItem(
                        modifier       = Modifier.weight(1f).fillMaxHeight(),
                        dayLabel       = dayLabel,
                        minutes        = minutes,
                        heightFraction = minutes.toFloat() / maxMinutes,
                        isHighlighted  = minutes == maxMinutes
                    )
                }
            }
        }
    }
}

@Composable
private fun BarChartItem(
    modifier: Modifier = Modifier,
    dayLabel: String,
    minutes: Int,
    heightFraction: Float,
    isHighlighted: Boolean
) {
    var triggered by remember { mutableStateOf(false) }
    val animatedFraction by animateFloatAsState(
        targetValue   = if (triggered) heightFraction else 0f,
        animationSpec = tween(durationMillis = 700),
        label         = "bar_$dayLabel"
    )
    LaunchedEffect(Unit) { triggered = true }

    val gradient  = Brush.verticalGradient(listOf(MlGradientStart, MlGradientEnd))
    val idleColor = Brush.verticalGradient(listOf(MlSurfaceContainerHighest, MlSurfaceContainerHighest))

    Column(
        modifier            = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier         = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .fillMaxHeight(animatedFraction)
                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                    .background(if (isHighlighted) gradient else idleColor)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text       = dayLabel,
            style      = MaterialTheme.typography.labelSmall,
            color      = if (isHighlighted) MlOnSurface else MlOnSurfaceVariant,
            fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// Mastery Distribution
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun MasteryDistributionCard(uiState: AnalyticsUiState) {
    val levels = listOf(
        uiState.masteryBeginner     to MlAccentCyan,
        uiState.masteryIntermediate to MlPrimary,
        uiState.masteryAdvanded     to MlSecondary // Sử dụng đúng tên biến trong AnalyticsUiState của bạn
    )

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MlSurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier            = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text  = "Mastery Distribution",
                style = MaterialTheme.typography.headlineSmall,
                color = MlOnSurface
            )
            levels.forEach { (level, barColor) ->
                MasteryProgressRow(level = level, barColor = barColor)
            }
        }
    }
}

@Composable
private fun MasteryProgressRow(level: MasteryLevel, barColor: Color) {
    var triggered by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue   = if (triggered) level.progress else 0f,
        animationSpec = tween(durationMillis = 900),
        label         = "progress_${level.label}"
    )
    LaunchedEffect(Unit) { triggered = true }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = level.label,            style = MaterialTheme.typography.labelMedium, color = MlOnSurface)
            Text(text = "${level.wordCount} words", style = MaterialTheme.typography.labelSmall,  color = MlOnSurfaceVariant)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50))
                .background(MlSurfaceContainerHighest)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50))
                    .background(barColor)
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// Activity Heatmap
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun ActivityHeatmapCard(uiState: AnalyticsUiState) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MlSurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier            = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text  = "Activity Map",
                style = MaterialTheme.typography.headlineSmall,
                color = MlOnSurface
            )
            Text(
                text  = "Your learning frequency over the last 35 days.",
                style = MaterialTheme.typography.bodySmall,
                color = MlOnSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))

            val weeks = uiState.activityMap.chunked(7)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                weeks.forEach { week ->
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        week.forEach { intensity ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        if (intensity == 0f) MlSurfaceContainerHighest
                                        else MlPrimary.copy(alpha = intensity)
                                    )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "Less", style = MaterialTheme.typography.labelSmall, color = MlOnSurfaceVariant)
                listOf(0f, 0.25f, 0.5f, 0.75f, 1f).forEach { alpha ->
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                if (alpha == 0f) MlSurfaceContainerHighest
                                else MlPrimary.copy(alpha = alpha)
                            )
                    )
                }
                Text(text = "More", style = MaterialTheme.typography.labelSmall, color = MlOnSurfaceVariant)
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// Preview
// ═══════════════════════════════════════════════════════════════════════════════

@Preview(showBackground = true, backgroundColor = 0xFFFCF8FF)
@Composable
fun AnalyticsScreenPreview() {
    MinLishMobileTheme {
        AnalyticsScreen()
    }
}