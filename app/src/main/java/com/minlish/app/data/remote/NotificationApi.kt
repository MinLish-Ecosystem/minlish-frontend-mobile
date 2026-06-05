package com.minlish.app.data.remote
import com.minlish.app.data.dto.response.ApiResponse
import com.minlish.app.data.dto.response.NotificationListDto
import com.minlish.app.data.dto.response.UnreadCountDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApi {
    @GET("notifications")
    suspend fun getNotifications(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("type") type: String? = null
    ): ApiResponse<NotificationListDto>
    @GET("notifications/unread-count")
    suspend fun getUnreadCount(): ApiResponse<UnreadCountDto>
    @PUT("notifications/{id}/read")
    suspend fun markAsRead(@Path("id") id: String): ApiResponse<Unit>
    @PUT("notifications/read-all")
    suspend fun markAllAsRead(): ApiResponse<Unit>
    @DELETE("notifications/{id}")
    suspend fun deleteNotification(@Path("id") id: String): ApiResponse<Unit>
}