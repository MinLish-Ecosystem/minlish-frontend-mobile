package com.minlish.app.data.remote

import com.minlish.app.data.dto.UserDto
import com.minlish.app.data.dto.request.LoginRequest
import com.minlish.app.data.dto.response.ApiResponse
import com.minlish.app.data.dto.response.LoginData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserAPI {

    @GET("user/profile")
    suspend fun getUserProfile(): ApiResponse<UserDto>
}