package com.minlish.app.data.dto

data class NotificationItemDto(
    val _id: String,
    val userId: String,
    val type: String,
    val title: String,
    val message: String,
    val isRead: Boolean,
    val createdAt: String,
    val data: Map<String, String> ?= null
)
data class PaginationDto(
    val page: Int,
    val limit: Int,
    val total: Int,
    val totalPages: Int
)
data class NotificationListDto(
    val data: List<NotificationItemDto>,
    val pagination: PaginationDto,
    val unreadCount: Int
)
data class UnreadCountDto(
    val unreadCount: Int
)