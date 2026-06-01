package com.minlish.app.data.remote

import com.minlish.app.data.dto.ApiResponse
import com.minlish.app.data.dto.DeleteFCMTokenRequest
import com.minlish.app.data.dto.LearningProfileDto
import com.minlish.app.data.dto.RegisterFCMTokenRequest
import com.minlish.app.data.dto.UpdateLearningProfileRequest
import com.minlish.app.data.dto.UpdateProfileRequest
import com.minlish.app.data.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PUT
import retrofit2.http.POST

interface UserApi {
    @GET("user/profile")
    suspend fun getProfile(): ApiResponse<UserDto>
    @PUT("user/profile")
    suspend fun updateProfile(@Body body: UpdateProfileRequest): ApiResponse<UserDto>
    @GET("user/learning-profile")
    suspend fun getLearningProfile(): ApiResponse<LearningProfileDto>
    @PUT("user/learning-profile")
    suspend fun updateLearningProfile(
        @Body body: UpdateLearningProfileRequest
    ): ApiResponse<LearningProfileDto>
    @POST("user/fcm-token")
    suspend fun registerFCMToken(@Body body: RegisterFCMTokenRequest): ApiResponse<Unit>
    @HTTP(method = "DELETE", path = "user/fcm-token", hasBody = true)
    suspend fun deleteFCMToken(@Body body: DeleteFCMTokenRequest): ApiResponse<Unit>
}