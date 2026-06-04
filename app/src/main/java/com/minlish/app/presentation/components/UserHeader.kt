package com.minlish.app.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import com.minlish.app.R
import com.minlish.app.presentation.screens.profile.ProfileViewModel

object AppColors {
    val Surface = Color(0xFFFCF8FF)
    val PrimaryText = Color(0xFF4648D4)
    val OnSurfaceVariant = Color(0xFF464554)
    val GradientStart = Color(0xFF667EEA)
    val GradientEnd = Color(0xFF764BA2)
}
object NotificationColors {
    val NormalBackground = Color.White
    val PressedBackground = Color.Blue
    val NormalIconTint = Color.Blue
    val PressedIconTint = Color.White
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    unreadCount: Int = 0,
    onNotificationClick: () -> Unit,
    onUserClick: () -> Unit={},
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.uiState.collectAsState()

    val textGradientBrush = Brush.linearGradient(
        colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
    )
    var isNotificationActive by remember { mutableStateOf(false) }
    Surface(
        color = Color.White,
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).clickable { onUserClick() }
                ) {
                    AsyncImage(
                        model = if (state.avatar.isNullOrEmpty()) R.drawable.profile_img else state.avatar,
                        contentDescription = "User Avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Text(
                    text = state.displayName.ifEmpty { "User" },
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        brush = textGradientBrush
                    )
                )
            }

            IconButton(
                onClick = {
                    isNotificationActive = true
                    onNotificationClick()
                    coroutineScope.launch {
                        kotlinx.coroutines.delay(150)
                        isNotificationActive = false
                    }
                },
                modifier = Modifier.background(
                    color = if (isNotificationActive) NotificationColors.PressedBackground else NotificationColors.NormalBackground,
                    shape = CircleShape
                )
            ) {
                BadgedBox(
                    badge = {
                        if (unreadCount > 0) {
                            Badge(containerColor = Color(0xFFEF4444)) {
                                Text(
                                    text = if (unreadCount > 99) "99+" else unreadCount.toString(),
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Notifications",
                        tint = if (isNotificationActive) NotificationColors.PressedIconTint else NotificationColors.NormalIconTint
                    )
                }
            }
        }
    }
}
