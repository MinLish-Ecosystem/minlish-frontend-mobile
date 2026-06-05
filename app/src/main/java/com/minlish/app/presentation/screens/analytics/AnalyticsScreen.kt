package com.minlish.app.presentation.screens.analytics

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
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
import com.minlish.app.ui.theme.*

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MinlishSurface),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MinlishPrimary)
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MinlishSurface),
        contentPadding = PaddingValues(start = 16.dp,end = 16.dp,top = 16.dp,bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (uiState.errorMessage != null) {
            item {
                ErrorAlertCard(message = uiState.errorMessage!!, onRetry = { viewModel.refresh() })
            }
        }
        item { AnalyticsHeader() }
        item { StatsBentoGrid(uiState) }
        item { WordsMasteredCard(uiState) }
        item { WeeklyProgressChart(uiState) }
        item { MasteryDistributionCard(uiState) }
        item { ActivityHeatmapCard(uiState) }
    }
}

@Composable
private fun AnalyticsHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "Analytics",
            style = MaterialTheme.typography.headlineLarge,
            color = MinlishOnSurface
        )
        Text(
            text = "Track your language learning journey.",
            style = MaterialTheme.typography.bodySmall,
            color = MinlishOnSurfaceVariant
        )
    }
}

@Composable
private fun StatsBentoGrid(uiState: AnalyticsUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Filled.LocalFireDepartment,
            iconTint = MinlishWarning,
            iconBgColor = MinlishWarning.copy(alpha = 0.15f),
            label = "Current Streak",
            value = "${uiState.currentStreak}",
            unit = "Days"
        )
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Filled.Timer,
            iconTint = MinlishInfo,
            iconBgColor = MinlishInfo.copy(alpha = 0.15f),
            label = "Time Spent",
            value = "${uiState.timeSpentHours}",
            unit = "Hrs"
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
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MinlishSurfaceLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MinlishOnSurfaceVariant
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MinlishOnSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.bodySmall,
                        color = MinlishOnSurfaceVariant,
                        modifier = Modifier.padding(bottom = 3.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun WordsMasteredCard(uiState: AnalyticsUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MinlishSurfaceLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MinlishSuccess.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = MinlishSuccess,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text  = "Words Mastered",
                        style = MaterialTheme.typography.labelMedium,
                        color = MinlishOnSurfaceVariant
                    )
                    Text(
                        text = "${uiState.wordsMastered}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MinlishOnSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Surface(
                color = MinlishSuccess.copy(alpha = 0.12f),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "+${uiState.wordsThisWeek} this week",
                    style = MaterialTheme.typography.labelSmall,
                    color = MinlishSuccess,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun WeeklyProgressChart(uiState: AnalyticsUiState) {
    val maxMinutes = uiState.weeklyMinutes.maxOf { it.second }.coerceAtLeast(1)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MinlishSurfaceLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "Weekly Progress",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MinlishOnSurface
                    )
                    Text(
                        text = "Minutes per day",
                        style = MaterialTheme.typography.bodySmall,
                        color = MinlishOnSurfaceVariant
                    )
                }
                TextButton(onClick = {}) {
                    Text(
                        text = "Details",
                        style = MaterialTheme.typography.labelMedium,
                        color = MinlishPrimary
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().height(140.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                uiState.weeklyMinutes.forEach { (dayLabel, minutes) ->
                    BarChartItem(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        dayLabel = dayLabel,
                        minutes = minutes,
                        heightFraction = minutes.toFloat() / maxMinutes,
                        isHighlighted = minutes == maxMinutes
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
        targetValue = if (triggered) heightFraction else 0f,
        animationSpec = tween(durationMillis = 700),
        label = "bar_$dayLabel"
    )
    LaunchedEffect(Unit) { triggered = true }

    val gradient  = Brush.verticalGradient(listOf(MinlishGradientStart, MinlishGradientEnd))
    val idleColor = Brush.verticalGradient(listOf(MinlishSurfaceHighest, MinlishSurfaceHighest))

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
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
            text = dayLabel,
            style = MaterialTheme.typography.labelSmall,
            color = if (isHighlighted) MinlishOnSurface else MinlishOnSurfaceVariant,
            fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun MasteryDistributionCard(uiState: AnalyticsUiState) {
    val levels = listOf(
        uiState.masteryBeginner to MinlishAccentCyan,
        uiState.masteryIntermediate to MinlishPrimary,
        uiState.masteryAdvanced to MinlishSecondary
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MinlishSurfaceLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Mastery Distribution",
                style = MaterialTheme.typography.headlineSmall,
                color = MinlishOnSurface
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
        targetValue = if (triggered) level.progress else 0f,
        animationSpec = tween(durationMillis = 900),
        label = "progress_${level.label}"
    )
    LaunchedEffect(Unit) { triggered = true }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = level.label, style = MaterialTheme.typography.labelMedium, color = MinlishOnSurface)
            Text(text = "${level.wordCount} words",style = MaterialTheme.typography.labelSmall,  color = MinlishOnSurfaceVariant)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50))
                .background(MinlishSurfaceHighest)
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

@Composable
private fun ActivityHeatmapCard(uiState: AnalyticsUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MinlishSurfaceLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Activity Map",
                style = MaterialTheme.typography.headlineSmall,
                color = MinlishOnSurface
            )
            Text(
                text = "Your learning frequency over the last 35 days.",
                style = MaterialTheme.typography.bodySmall,
                color = MinlishOnSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))

            val weeks = uiState.activityMap.chunked(7)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                weeks.forEach { week ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        week.forEach { intensity ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        if (intensity == 0f) MinlishSurfaceHighest
                                        else MinlishPrimary.copy(alpha = intensity)
                                    )
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "Less", style = MaterialTheme.typography.labelSmall, color = MinlishOnSurfaceVariant)
                listOf(0f, 0.25f, 0.5f, 0.75f, 1f).forEach { alpha ->
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                if (alpha == 0f) MinlishSurfaceHighest
                                else MinlishPrimary.copy(alpha = alpha)
                            )
                    )
                }
                Text(text = "More", style = MaterialTheme.typography.labelSmall, color = MinlishOnSurfaceVariant)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFCF8FF)
@Composable
fun AnalyticsScreenPreview() {
    MinLishMobileTheme {
        AnalyticsScreen()
    }
}

@Composable
private fun ErrorAlertCard(message: String, onRetry: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3CD)),
        border = BorderStroke(1.dp, Color(0xFFFFEBAA))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Warning",
                tint = Color(0xFF856404),
                modifier = Modifier.size(24.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Lỗi kết nối mạng",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF856404)
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF856404)
                )
            }
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF856404)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("Thử lại", color = Color.White, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}