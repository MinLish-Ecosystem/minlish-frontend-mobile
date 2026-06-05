package com.minlish.app.data.remote

import com.minlish.app.data.dto.response.ApiResponse
import com.minlish.app.data.dto.response.LearningDto
import retrofit2.http.GET

interface LearningApi {
    @GET("learning/home")
    suspend fun getLearningSet(): ApiResponse<LearningDto>
}
