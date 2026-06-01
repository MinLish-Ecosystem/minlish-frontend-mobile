package com.minlish.app.presentation.screens.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minlish.app.data.dto.NotificationItemDto
import com.minlish.app.data.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NotificationUiState(
    val notifications: List<NotificationItemDto> = emptyList(),
    val unreadCount: Int = 0,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = true,
    val currentPage: Int = 1,
    val errorMessage: String? = null
)

class NotificationViewModel : ViewModel() {
    private val repository = NotificationRepository()
    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.getNotifications(page = 1)
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            notifications = result.data,
                            unreadCount = result.unreadCount,
                            currentPage = 1,
                            hasMore = result.pagination.page < result.pagination.totalPages,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message)
                    }
                }
        }
    }

    fun loadMore() {
        val state = _uiState.value
        if (state.isLoadingMore || !state.hasMore) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            val nextPage = state.currentPage + 1
            repository.getNotifications(page = nextPage)
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            notifications = it.notifications + result.data,
                            unreadCount = result.unreadCount,
                            currentPage = nextPage,
                            hasMore = result.pagination.page < result.pagination.totalPages,
                            isLoadingMore = false
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoadingMore = false) }
                }
        }
    }

    fun markAsRead(notificationId: String) {
        _uiState.update { state ->
            state.copy(
                notifications = state.notifications.map {
                    if (it._id == notificationId) it.copy(isRead = true) else it
                },
                unreadCount = (state.unreadCount - 1).coerceAtLeast(0)
            )
        }
        viewModelScope.launch {
            repository.markAsRead(notificationId).onFailure {
                // Rollback nếu thất bại
                _uiState.update { state ->
                    state.copy(
                        notifications = state.notifications.map {
                            if (it._id == notificationId) it.copy(isRead = false) else it
                        },
                        unreadCount = state.unreadCount + 1
                    )
                }
            }
        }
    }

    fun markAllAsRead() {
        // Optimistic update
        _uiState.update { state ->
            state.copy(
                notifications = state.notifications.map { it.copy(isRead = true) },
                unreadCount = 0
            )
        }
        viewModelScope.launch {
            repository.markAllAsRead().onFailure {
                loadNotifications() // Reload nếu thất bại
            }
        }
    }

    fun deleteNotification(notificationId: String) {
        val deletedItem = _uiState.value.notifications.find { it._id == notificationId }
        _uiState.update { state ->
            state.copy(
                notifications = state.notifications.filter { it._id != notificationId },
                unreadCount = if (deletedItem?.isRead == false)
                    (state.unreadCount - 1).coerceAtLeast(0) else state.unreadCount
            )
        }
        viewModelScope.launch {
            repository.deleteNotification(notificationId).onFailure {
                loadNotifications()
            }
        }
    }
}