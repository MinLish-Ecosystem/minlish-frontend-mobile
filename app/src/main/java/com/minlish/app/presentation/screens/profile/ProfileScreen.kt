package com.minlish.app.presentation.screens.profile

import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
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
import androidx.compose.runtime.getValue
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
import com.minlish.app.ui.theme.*
import com.minlish.app.R
import androidx.compose.runtime.collectAsState
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.minlish.app.presentation.screens.auth.viewmodels.LoginUiEvent
import kotlinx.coroutines.flow.collectLatest


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(
    onLogOutSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is ProfileUiEvent.LogOutSuccess -> {
                    onLogOutSuccess()
                }
                is ProfileUiEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val inputStream = context.contentResolver.openInputStream(it)
                val bytes = inputStream?.readBytes()
                inputStream?.close()
                if (bytes != null) {
                    val base64String = "data:image/jpeg;base64," + Base64.encodeToString(bytes, Base64.NO_WRAP)
                    viewModel.uploadAvatar(base64String)
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Image Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            Toast.makeText(context, "Update Success!", Toast.LENGTH_SHORT).show()
            viewModel.dismissSuccess()
        }
    }
    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.dismissError()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MinlishSurface)
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MinlishPrimary)
                }
            } else {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    ProfileBannerSection(
                        userName = state.displayName.ifEmpty { "User" },
                        userLevel = state.userLevel,
                        joinYear = state.joinYear,
                        userAvatarUrl = state.avatar,
                        onAvatarClick = { imagePickerLauncher.launch("image/*") }
                    )

                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        PersonalInfoCard(
                            displayName = state.displayName,
                            email = state.email,
                            onDisplayNameChange = { viewModel.updateDisplayName(it) },
                            onEmailChange = { viewModel.updateEmail(it) }
                        )
                        LearningGoalsCard(
                            selectedGoal = state.selectedGoal,
                            dailyWordTarget = state.dailyWordTarget,
                            dailyReviewTarget = state.dailyReviewTarget,
                            currentStreak = state.currentStreak,
                            onGoalChange = { viewModel.updateSelectedGoal(it) },
                            onWordTargetChange = { viewModel.updateWordTarget(it) },
                            onReviewTargetChange = { viewModel.updateReviewTarget(it) }
                        )

                        AppSettingsCard(
                            pushNotifications = state.pushNotifications,
                            emailNotifications = state.emailNotifications,
                            reminderTime = state.reminderTime,
                            darkMode = state.darkModeEnabled,
                            onPushNotificationsChange = { viewModel.updatePushNotifications(it) },
                            onEmailNotificationsChange = { viewModel.updateEmailNotifications(it) },
                            onReminderTimeChange = { viewModel.updateReminderTime(it) },
                            onDarkModeChange = { viewModel.updateDarkMode(it) },
                            onLogOutClick = {
                                viewModel.logout()
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        SaveButton(
                            isSaving = state.isSaving,
                            onSaveClick = { viewModel.saveChanges() }
                        )
                    }
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
    userAvatarUrl: String?,
    onAvatarClick: () -> Unit
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
                    )
                    .clickable { onAvatarClick() },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MinlishSurfaceContainerHigh),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = if (userAvatarUrl.isNullOrEmpty()) R.drawable.profile_img else userAvatarUrl,
                        contentDescription = "User Avatar",
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
                keyboardType = KeyboardType.Email,
                readOnly = true
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
    keyboardType: KeyboardType = KeyboardType.Text,
    readOnly: Boolean = false
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
            readOnly = readOnly,
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
            onExpandedChange = { expanded = it }
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
    emailNotifications: Boolean,
    reminderTime: String,
    darkMode: Boolean,
    onPushNotificationsChange: (Boolean) -> Unit,
    onEmailNotificationsChange: (Boolean) -> Unit,
    onReminderTimeChange: (String) -> Unit,
    onDarkModeChange: (Boolean) -> Unit,
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
                subtitle = "Daily reminders & streaks",
                checked = pushNotifications,
                onCheckedChange = onPushNotificationsChange,
                activeColor = MinlishSuccess
            )

            SettingsToggleRow(
                title = "Email Notifications",
                subtitle = "Receive emails when offline",
                checked = emailNotifications,
                onCheckedChange = onEmailNotificationsChange,
                activeColor = MinlishInfo
            )

            SettingsTimeRow(
                title = "Reminder Time",
                subtitle = "When to send daily notification",
                time = reminderTime,
                onTimeChange = onReminderTimeChange
            )

            SettingsToggleRow(
                title = "Dark Mode",
                subtitle = "Coming soon",
                checked = darkMode,
                onCheckedChange = onDarkModeChange,
                activeColor = Color(0xFF6366F1)
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
    val hour = time.split(":").getOrNull(0)?.toIntOrNull()?:20
    val minute = time.split(":").getOrNull(1)?.toIntOrNull()?: 0

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
private fun SaveButton(
    isSaving: Boolean = false,
    onSaveClick: () -> Unit
) {
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
        val backgroundModifier = if (isSaving) {
            Modifier.background(Color(0xFFa5b4fc))
        } else {
            Modifier.background(MinlishGradient)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .then(backgroundModifier)
                .clickable(enabled = !isSaving) { onSaveClick() },
            contentAlignment = Alignment.Center
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Save Changes",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(onLogOutSuccess = {})
}