package com.minlish.app.presentation.screens.notifications

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.app.data.dto.response.NotificationItemDto
import com.minlish.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    onBackClick: () -> Unit,
    viewModel: NotificationViewModel,
    onNotificationClick: (NotificationItemDto) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleItem >= totalItems - 3 && state.hasMore && !state.isLoadingMore
        }
    }
    LaunchedEffect(shouldLoadMore) { if (shouldLoadMore) viewModel.loadMore() }
    val unreadNotifications = state.notifications.filter { !it.isRead }
    val readNotifications = state.notifications.filter { it.isRead }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Thông báo",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        if (state.unreadCount > 0) {
                            Badge(containerColor = Color(0xFFEF4444)) {
                                Text(
                                    text = state.unreadCount.toString(),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (state.unreadCount > 0) {
                        TextButton(onClick = { viewModel.markAllAsRead() }) {
                            Text(
                                text = "Đọc hết",
                                color = MinlishPrimary,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF111827)
                )
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MinlishPrimary)
            }
        } else if (state.notifications.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.NotificationsOff,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color(0xFFD1D5DB)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Chưa có thông báo nào",
                        fontSize = 16.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (unreadNotifications.isNotEmpty()) {
                    item {
                        SectionHeader(title = "MỚI")
                    }
                    itemsIndexed(
                        items = unreadNotifications,
                        key = { _, item -> item._id }
                    ) { _, notification ->
                        NotificationCard(
                            notification = notification,
                            onClick = {
                                viewModel.markAsRead(notification._id)
                                onNotificationClick(notification)
                            }
                        )
                    }
                }
                if (readNotifications.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        SectionHeader(title = "TRƯỚC ĐÓ")
                    }
                    itemsIndexed(
                        items = readNotifications,
                        key = { _, item -> item._id }
                    ) { _, notification ->
                        NotificationCard(
                            notification = notification,
                            onClick = { onNotificationClick(notification) }
                        )
                    }
                }
                if (state.isLoadingMore) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp,
                                color = MinlishPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF64748B),
        letterSpacing = 1.sp,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
private fun NotificationCard(
    notification: NotificationItemDto,
    onClick: () -> Unit
) {
    val isUnread = !notification.isRead
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = if (isUnread) 2.dp else 1.dp,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .let { if (!isUnread) it.then(Modifier.background(Color.White.copy(alpha = 0.75f))) else it }
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            if (isUnread) {
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(56.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFF3B82F6))
                )
            }
            NotificationIcon(type = notification.type)
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = notification.title,
                        fontSize = 15.sp,
                        fontWeight = if (isUnread) FontWeight.SemiBold else FontWeight.Medium,
                        color = Color(0xFF1E293B),
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = formatRelativeTime(notification.createdAt),
                        fontSize = 12.sp,
                        color = if (isUnread) Color(0xFF3B82F6) else Color(0xFF94A3B8),
                        fontWeight = if (isUnread) FontWeight.Medium else FontWeight.Normal
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notification.message,
                    fontSize = 14.sp,
                    color = Color(0xFF64748B),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )
            }
            if (isUnread) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF3B82F6))
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
private fun NotificationIcon(type: String) {
    data class IconInfo(val icon: ImageVector, val tint: Color, val bg: Color)
    val info = when (type) {
        "daily_reminder" -> IconInfo(Icons.Filled.Schedule, Color(0xFF3B82F6), Color(0xFFDBEAFE))
        "review_due" -> IconInfo(Icons.Filled.Psychology, Color(0xFF8B5CF6), Color(0xFFEDE9FE))
        "streak_milestone" -> IconInfo(Icons.Filled.LocalFireDepartment, Color(0xFFEF4444), Color(0xFFFEE2E2))
        "achievement" -> IconInfo(Icons.Filled.EmojiEvents, Color(0xFFEAB308), Color(0xFFFEF9C3))
        "system" -> IconInfo(Icons.Filled.Info, Color(0xFF06B6D4), Color(0xFFCFFAFE))
        else -> IconInfo(Icons.Filled.Notifications, Color(0xFF64748B), Color(0xFFF1F5F9))
    }
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(info.bg),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = info.icon,
            contentDescription = null,
            tint = info.tint,
            modifier = Modifier.size(22.dp)
        )
    }
}

private fun formatRelativeTime(isoDate: String): String {
    return try {
        val formats = listOf(
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()),
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        )
        formats.forEach { it.timeZone = TimeZone.getTimeZone("UTC") }
        val date = formats.firstNotNullOfOrNull { fmt ->
            try { fmt.parse(isoDate) } catch (e: Exception) { null }
        } ?: return isoDate
        val now = System.currentTimeMillis()
        val diff = now - date.time
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val days = TimeUnit.MILLISECONDS.toDays(diff)
        when {
            minutes < 1 -> "Vừa xong"
            minutes < 60 -> "${minutes} phút trước"
            hours < 24 -> "${hours} giờ trước"
            days == 1L -> "Hôm qua"
            days < 7 -> "${days} ngày trước"
            else -> SimpleDateFormat("dd/MM", Locale.getDefault()).format(date)
        }
    } catch (e: Exception) {
        isoDate
    }
}