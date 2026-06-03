package com.minlish.app.data.remote

import com.minlish.app.data.dto.LearningDto
import com.minlish.app.data.dto.VocabSetDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LearningApi {
    @GET("learning/home")
    suspend fun getLearningSet(): ApiResponse<LearningDto>
}
