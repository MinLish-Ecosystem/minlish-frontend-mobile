package com.minlish.app.presentation.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.app.ui.theme.*
import com.minlish.app.R
import com.minlish.app.presentation.components.AppHeader
import com.minlish.app.ui.components.Footer

@Composable
fun ProfileScreen(
    userName: String = "Alex Johnson",
    userLevel: String = "Intermediate Learner",
    joinYear: String = "2023",
    onNotificationClick: () -> Unit = {},
    onUserClick: () -> Unit = {}
) {
    var displayName by remember { mutableStateOf("Alex Johnson") }
    var email by remember { mutableStateOf("alex@example.com") }

    var selectedGoal by remember { mutableStateOf("General") }
    var dailyWordTarget by remember { mutableIntStateOf(25) }
    var dailyReviewTarget by remember { mutableIntStateOf(30) }
    val currentStreak = 14

    var currentRoute by remember { mutableStateOf("Profile") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MinlishSurface)
            .verticalScroll(rememberScrollState())
    ) {
        AppHeader(
            userName = userName,
            userAvatarId = R.drawable.profile_img,
            onNotificationClick = onNotificationClick,
            onUserClick = onUserClick,
        )
        ProfileBannerSection(
            userName = userName,
            userLevel = userLevel,
            joinYear = joinYear
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            PersonalInfoCard(
                displayName = displayName,
                email = email,
                onDisplayNameChange = {displayName = it},
                onEmailChange = {email = it}
            )
            LearningGoalsCard(
                selectedGoal = selectedGoal,
                dailyWordTarget = dailyWordTarget,
                dailyReviewTarget = dailyReviewTarget,
                currentStreak = currentStreak,
                onGoalChange = { selectedGoal = it },
                onWordTargetChange = { dailyWordTarget = it },
                onReviewTargetChange = { dailyReviewTarget = it }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Footer(
            currentRoute = currentRoute,
            onNavigate = { newRoute ->
                currentRoute = newRoute
            }
        )
    }
}

@Composable
private fun ProfileBannerSection(
    userName: String,
    userLevel: String,
    joinYear: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(216.dp + 48.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MinlishPrimary,
                            Color(0xFF8127CF)
                        )
                    )
                )
                .align(Alignment.TopCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(112.dp)
                    .clip(CircleShape)
                    .border(
                        width = 4.dp,
                        color = MinlishSurfaceLowest,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MinlishSurfaceContainerHigh),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.profile_img),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Text(
                text = userName,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111827)
            )

            Text(
                text = "$userLevel • Joined $joinYear",
                fontSize = 14.sp,
                color = MinlishOnSurfaceVariant
            )
        }
    }
}

@Composable
private fun PersonalInfoCard(
    displayName: String,
    email: String,
    onDisplayNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MinlishSurfaceLowest,
        shadowElevation = 4.dp,
        border = BorderStroke(1.dp, MinlishOnSurfaceVariant.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileCardHeader(
                icon = Icons.Outlined.Person,
                title = "Personal Info",
                iconTint = MinlishPrimary900
            )
            
            ProfileTextField(
                label = "Display Name",
                value = displayName,
                onValueChange = onDisplayNameChange,
                leadingIcon = Icons.Outlined.Badge,
                keyboardType = KeyboardType.Text
            )

            ProfileTextField(
                label = "Email",
                value = email,
                onValueChange = onEmailChange,
                leadingIcon = Icons.Outlined.Email,
                keyboardType = KeyboardType.Email
            )
        }
    }
}

@Composable
fun ProfileCardHeader(
    icon: ImageVector,
    title: String,
    iconTint: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = iconTint
        )
    }
}

@Composable
private fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MinlishOnSurfaceVariant,
            letterSpacing = 0.1.sp
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = MinlishOutline,
                    modifier = Modifier.size(20.dp)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MinlishPrimary,
                unfocusedBorderColor = MinlishOutlineVariant.copy(alpha = 0.5f),
                focusedContainerColor = Color(0xFFF9FAFB),
                unfocusedContainerColor = Color(0xFFF9FAFB),
                cursorColor = MinlishPrimary
            )
        )
    }
}

@Composable
private fun LearningGoalsCard(
    selectedGoal: String,
    dailyWordTarget: Int,
    dailyReviewTarget: Int,
    currentStreak: Int,
    onGoalChange: (String) -> Unit,
    onWordTargetChange: (Int) -> Unit,
    onReviewTargetChange: (Int) -> Unit
) {
    val goalOptions = listOf("General", "IELTS", "TOEIC", "Business", "Travel", "Other")

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MinlishSurfaceLowest,
        shadowElevation = 4.dp,
        border = BorderStroke(1.dp, MinlishOutlineVariant.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ProfileCardHeader(
                icon = Icons.Outlined.TrackChanges,
                title = "Learning Goals",
                iconTint = Color(0xFF06B6D4)
            )

            GoalDropdown(
                selectedGoal = selectedGoal,
                options = goalOptions,
                onGoalChange = onGoalChange
            )

            TargetSlider(
                label = "Daily Word Target",
                value = dailyWordTarget,
                valueUnit = "words",
                min = 1,
                max = 100,
                onValueChange = onWordTargetChange
            )

            TargetSlider(
                label  = "Daily Review Target",
                value = dailyReviewTarget,
                valueUnit = "words",
                min = 5,
                max = 200,
                onValueChange = onReviewTargetChange
            )

            StreakBanner(currentStreak = currentStreak)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoalDropdown(
    selectedGoal: String,
    options: List<String>,
    onGoalChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "Learning Goal",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MinlishOnSurfaceVariant
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {expanded != expanded}
        ) {
            OutlinedTextField(
                value = selectedGoal,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MinlishPrimary,
                    unfocusedBorderColor = MinlishOutlineVariant.copy(alpha = 0.5f),
                    focusedContainerColor = Color(0xFFF9FAFB),
                    unfocusedContainerColor = Color(0xFFF9FAFB)
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {expanded = false}
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {Text(option)},
                        onClick = {
                            onGoalChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TargetSlider(
    label: String,
    value: Int,
    valueUnit: String,
    min: Int,
    max: Int,
    onValueChange: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MinlishOnSurfaceVariant
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "$value",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MinlishPrimary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = valueUnit,
                    fontSize = 14.sp,
                    color = MinlishOnSurfaceVariant
                )
            }
        }

        Slider(
            value = value.toFloat(),
            onValueChange = {onValueChange(it.toInt())},
            valueRange = min.toFloat()..max.toFloat(),
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = MinlishPrimary,
                activeTrackColor = MinlishPrimary,
                inactiveTickColor = Color(0xFFE5E7EB)
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "$min", fontSize = 12.sp, color = MinlishOutline)
            Text(text = "$max", fontSize = 12.sp, color = MinlishOutline)
        }
    }
}

@Composable
private fun StreakBanner(currentStreak: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF0F4FF)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "Current Streak",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF111827)
                )
                Text(
                    text = "Keep it up!",
                    fontSize = 14.sp,
                    color = MinlishOnSurfaceVariant
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.LocalFireDepartment,
                    contentDescription = null,
                    tint = Color(0xFFF59E0B),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "$currentStreak Days",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFF59E0B)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}