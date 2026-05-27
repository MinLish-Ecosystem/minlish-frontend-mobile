package com.minlish.app.presentation.screens.welcome

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.CorporateFare
import androidx.compose.material.icons.outlined.FlightTakeoff
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.app.presentation.screens.auth.components.ProgressHeader
import com.minlish.app.ui.theme.*

data class LearningGoalOption(
    val id: String,
    val label: String,
    val  icon: ImageVector,
    val description: String = "",
    val isFullWidth: Boolean = false
)

private val learningGoalOptions = listOf(
    LearningGoalOption(
        id = "ielts",
        label = "IELTS",
        icon = Icons.AutoMirrored.Outlined.Assignment
    ),

    LearningGoalOption(
        id = "toeic",
        label = "TOEIC",
        icon = Icons.Outlined.CorporateFare
    ),

    LearningGoalOption(
        id = "business",
        label = "Business",
        icon = Icons.Outlined.BusinessCenter
    ),

    LearningGoalOption(
        id = "travel",
        label = "Travel",
        icon = Icons.Outlined.FlightTakeoff
    ),

    LearningGoalOption(
        id = "general",
        label = "General Learning",
        icon = Icons.Outlined.Forum,
        description = "Everyday conversation and fluency",
        isFullWidth = true
    )
)

@Composable
fun LearningGoalScreen(
    currentStep: Int = 2,
    totalSteps: Int = 2,
    onBackClick: () -> Unit = {}
) {
    var selectedId by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MinlishSurface)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        ProgressHeader(
            currentStep = currentStep,
            totalSteps = totalSteps,
            onBackClick = onBackClick
        )
        Spacer(modifier = Modifier.height(32.dp))
        LearningGoalTitle()

        Spacer(modifier = Modifier.height(32.dp))
        LearningGoalGrid(
            selectedId = selectedId,
            onSelect = { selectedId = it }
        )
    }
}

@Composable
private fun LearningGoalTitle() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "What is your primary\nLearning goal?",
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            color = MinlishOutline,
            lineHeight = 41.sp,
            letterSpacing = (-0.5).sp
        )
        Text(
            text = "We'll tailor your daily vocabulary and exercises to match your focus.",
            fontSize = 16.sp,
            color = MinlishOnSurfaceVariant,
            lineHeight = 25.sp
        )
    }
}

@Composable
private fun LearningGoalGrid(
    selectedId: String?,
    onSelect: (String) -> Unit
) {
    val gridOptions = learningGoalOptions.filter {!it.isFullWidth}
    val fullWidthOption = learningGoalOptions.first{it.isFullWidth}
    val rows = gridOptions.chunked(2)

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            rows.forEach { rowOptions ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowOptions.forEach { option ->
                        GoalOptionCard(
                            option = option,
                            isSelected = selectedId == option.id,
                            onSelect = { onSelect(option.id) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

        GoalOptionCardWide(
            option = fullWidthOption,
            isSelected = selectedId == fullWidthOption.id,
            onSelect = { onSelect(fullWidthOption.id)}
        )
    }
}

@Composable
private fun GoalOptionCard(
    option: LearningGoalOption,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onSelect,
        modifier = modifier.aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) Color(0xFFF0F4FF) else MinlishSurfaceLowest,
        border = BorderStroke(
            width = 2.dp,
            color = if (isSelected) MinlishPrimary else MinlishSurfaceContainerHigh
        ),
        shadowElevation = if (isSelected) 8.dp else 4.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = MinlishPrimary,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(20.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) MinlishPrimary
                            else Color(0xFFF0F4FF)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = option.icon,
                        contentDescription = null,
                        tint = if (isSelected) Color.White else MinlishPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = option.label,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) MinlishPrimary else MinlishOnSurface
                )
            }
        }
    }
}

@Composable
private fun GoalOptionCardWide(
    option: LearningGoalOption,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    Surface(
        onClick = onSelect,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) Color(0xFFF0F4FF) else MinlishSurfaceLowest,
        border = BorderStroke(
            width = 2.dp,
            color = if (isSelected) MinlishPrimary else MinlishSurfaceContainerHigh
        ),
        shadowElevation = if (isSelected) 8.dp else 4.dp
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MinlishPrimary
                        else Color(0xFFF0F4FF)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = option.icon,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else MinlishPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = option.label,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) MinlishPrimary else MinlishOnSurface
                )

                Text(
                    text = option.description,
                    fontSize = 14.sp,
                    color = MinlishOnSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LearningGoalScreenPreview() {
    LearningGoalScreen()
}

