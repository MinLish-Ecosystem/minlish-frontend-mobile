package com.minlish.app.presentation.screens.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.minlish.app.ui.theme.*
import com.minlish.app.presentation.components.AppHeader
import com.minlish.app.presentation.components.Footer
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    currentRoute: String = "profile",
    onNavigate: (String) -> Unit = {},
    userLevel: String = "Intermediate Learner",
    onNotificationClick: () -> Unit = {},
    onUserClick: () -> Unit = {},
    onLogOutClick: () -> Unit
) {
    val user by viewModel.user.collectAsState()

    var displayName by remember(user) {
        mutableStateOf(user?.name ?: "Anonymous")
    }

    var email by remember(user) {
        mutableStateOf(user?.email ?: "example@gmail.com")
    }

    var avatar by remember(user) {
        mutableStateOf(user?.avatar ?: "https://api.dicebear.com/7.x/avataaars/png?seed=QuangLe")
    }

    var joinYear by remember(user) {
        val year = try {
            user?.createdAt?.let { java.time.Instant.parse(it).atZone(java.time.ZoneId.of("UTC")).year } ?: 2023
        } catch (e: Exception) { 2023 }
        mutableIntStateOf(year)
    }

    var selectedGoal by remember { mutableStateOf("General") }
    var dailyWordTarget by remember { mutableIntStateOf(25) }
    var dailyReviewTarget by remember { mutableIntStateOf(30) }
    val currentStreak = 14

    var pushNotifications by remember { mutableStateOf(true) }
    var reminderTime by remember { mutableStateOf("20:00") }
    var darkMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppHeader(
                onNotificationClick = onNotificationClick,
                onUserClick = onUserClick,
            )
        },
        containerColor = Color(0xFFF9FAFB),
        bottomBar = {
            Footer(
                currentRoute = currentRoute,
                onNavigate = onNavigate
            )
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MinlishSurface)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {

                ProfileBannerSection(
                    userName = displayName,
                    userLevel = userLevel,
                    joinYear = joinYear.toString(),
                    avatar = avatar
                )

                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    PersonalInfoCard(
                        displayName = displayName,
                        email = email,
                        onDisplayNameChange = { displayName = it },
                        onEmailChange = { email = it }
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

                    AppSettingsCard(
                        pushNotifications = pushNotifications,
                        reminderTime = reminderTime,
                        darkMode = darkMode,
                        onPushNotificationsChange = { pushNotifications = it },
                        onReminderTimeChange = { reminderTime = it },
                        onLogOutClick = onLogOutClick
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    SaveButton(                               // ← thêm dính đáy
                        onSaveClick = {
                            // TODO: gọi ProfileViewModel.save()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileBannerSection(
    userName: String,
    userLevel: String,
    joinYear: String,
    avatar: String
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
                    AsyncImage(
                        model = avatar,
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

@Composable
private fun AppSettingsCard(
    pushNotifications: Boolean,
    reminderTime: String,
    darkMode: Boolean,
    onPushNotificationsChange: (Boolean) -> Unit,
    onReminderTimeChange: (String) -> Unit,
    onLogOutClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MinlishSurfaceLowest,
        shadowElevation = 4.dp,
        border = BorderStroke(1.dp, MinlishOutlineVariant.copy(0.2f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ProfileCardHeader(
                icon = Icons.Outlined.Settings,
                title = "App Settings",
                iconTint = Color(0xFF581C87)
            )

            SettingsToggleRow(
                title = "Push Notifications",
                subtitle =  "Daily reminders & streaks",
                checked = pushNotifications,
                onCheckedChange = onPushNotificationsChange,
                activeColor = MinlishSuccess
            )

            SettingsTimeRow(
                title = "Reminder Time",
                subtitle = "When to send daily notification",
                time = reminderTime,
                onTimeChange = onReminderTimeChange
            )

            HorizontalDivider(color = Color(0xFFF3F4F6))
            TextButton(
                onClick = onLogOutClick,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Logout,
                            contentDescription = null,
                            tint = MinlishError,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Log Out",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MinlishError
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    activeColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF111827)
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = MinlishOnSurfaceVariant
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = activeColor,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFE5E7EB)
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTimeRow(
    title: String,
    subtitle: String,
    time: String,
    onTimeChange: (String) -> Unit
) {
    var showTimePicker by remember { mutableStateOf(false) }
    var hour = time.split(":").getOrNull(0)?.toIntOrNull()?:20
    var minute = time.split(":").getOrNull(1)?.toIntOrNull()?: 0

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = hour,
            initialMinute = minute
        )
        AlertDialog(
            onDismissRequest = {showTimePicker = false},
            confirmButton = {
                TextButton(onClick = {
                    val h = timePickerState.hour.toString().padStart(2,'0')
                    val m = timePickerState.minute.toString().padStart(2,'0')
                    onTimeChange("$h:$m")
                    showTimePicker = false
                }) {
                    Text("OK", color = MinlishPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = {showTimePicker = false}) {
                    Text("Cancel", color = MinlishOnSurfaceVariant)
                }
            },
            text = { TimePicker(state = timePickerState) }
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF111827)
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = MinlishOnSurfaceVariant
            )
        }

        Surface(
            onClick = {showTimePicker = true},
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFF9FAFB),
            border = BorderStroke(1.dp, MinlishOnSurfaceVariant.copy(alpha = 0.5f))
        ) {
            Text(
                text = time,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF111827),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun SaveButton(onSaveClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color(0xFFF9FAFB).copy(alpha = 0.9f),
                        Color(0xFFF9FAFB)
                    )
                )
            )
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 24.dp)
    ) {
        Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MinlishGradient)
                    .clickable {onSaveClick()},
                contentAlignment = Alignment.Center
            ) {
            Text(
                text = "Save Changes",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(onLogOutClick = {})
}