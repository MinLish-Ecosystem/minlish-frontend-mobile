package com.minlish.app.data.remote


import com.minlish.app.data.dto.FlashCardTestDto
import retrofit2.http.GET
import retrofit2.http.Path

interface FlashCardApi{
    @GET("learning/flashcard/{id}")
    suspend fun getTestSet(
        @Path("id") id: String
    ): FlashCardTestDto
}