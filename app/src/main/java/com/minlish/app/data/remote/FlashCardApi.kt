package com.minlish.app.data.remote


import com.minlish.app.data.dto.FlashCardTestDto
import retrofit2.http.GET
import retrofit2.http.Path

interface FlashCardApi{
    @GET("learning/flashcard-test")
    suspend fun getTestSet(): ApiResponse<FlashCardTestDto>
}