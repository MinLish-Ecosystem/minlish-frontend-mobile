package com.minlish.app.util

import com.minlish.app.data.repository.NotificationRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object NotificationBadgeManager {
    private val repository = NotificationRepository()
    private val _badgeCount = MutableStateFlow(0)
    val badgeCount: StateFlow<Int> = _badgeCount.asStateFlow()

    private var pollingJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    /**
     * Bắt đầu vòng lặp polling mỗi 60 giây khi user đã login
     */
    fun startPolling() {
        if (pollingJob?.isActive == true) return
        pollingJob = scope.launch {
            while (isActive) {
                refreshBadge()
                delay(60_000L) // 60 giây
            }
        }
    }

    /**
     * Dừng polling và reset số badge (gọi khi Logout)
     */
    fun stopPolling() {
        pollingJob?.cancel()
        _badgeCount.value = 0
    }

    /**
     * Cập nhật thủ công hoặc gọi ngầm để tải unread count từ API
     */
    fun refreshBadge() {
        scope.launch {
            repository.getUnreadCount().onSuccess { count ->
                _badgeCount.value = count
            }
        }
    }

    /**
     * Cập nhật nóng số lượng badge từ bên ngoài (ví dụ sau khi markAsRead)
     */
    fun updateBadgeDirectly(count: Int) {
        _badgeCount.value = count
    }
}
