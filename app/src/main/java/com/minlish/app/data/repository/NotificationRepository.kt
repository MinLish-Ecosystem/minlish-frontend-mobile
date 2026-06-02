package com.minlish.app.data.repository

import com.minlish.app.data.dto.NotificationListDto
import com.minlish.app.di.NetworkModule

class NotificationRepository {
    private val api = NetworkModule.notificationApi
    suspend fun getNotifications(page: Int, limit: Int = 20): Result<NotificationListDto> =
        runCatching {
            api.getNotifications(page, limit).data
                ?: throw Exception("Notification data is null")
        }
    suspend fun getUnreadCount(): Result<Int> = runCatching {
        api.getUnreadCount().data?.unreadCount ?: 0
    }
    suspend fun markAsRead(id: String): Result<Unit> = runCatching {
        api.markAsRead(id)
        Unit
    }
    suspend fun markAllAsRead(): Result<Unit> = runCatching {
        api.markAllAsRead()
        Unit
    }
    suspend fun deleteNotification(id: String): Result<Unit> = runCatching {
        api.deleteNotification(id)
        Unit
    }
}