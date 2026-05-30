package com.minlish.app.data.remote

import com.minlish.app.data.dto.FlashCardTestDto
import com.minlish.app.data.dto.PracticeModeDto
import retrofit2.http.GET
import retrofit2.http.Path

interface PracticeApi{
    @GET("practice/{id}")
    suspend fun getPracticeMode(
        @Path("id") id: String
    ): PracticeModeDto
}