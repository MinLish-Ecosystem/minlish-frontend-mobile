package com.minlish.app.data.remote

import com.minlish.app.data.dto.response.ApiResponse
import com.minlish.app.data.dto.response.PracticeModeDto
import retrofit2.http.GET

interface PracticeApi{
    @GET("practice//practice-modes")
    suspend fun getPracticeMode(): ApiResponse<PracticeModeDto>
}