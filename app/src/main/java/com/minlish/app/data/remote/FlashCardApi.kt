package com.minlish.app.data.remote

import com.minlish.app.data.dto.FlashCardTestDto
import retrofit2.http.*

data class SetContextDto(
    val setId: String,
    val setName: String?,
    val colorTheme: String?
)

data class LearningCardDto(
    val id: String,
    val word: String,
    val pronunciation: String?,
    val partOfSpeech: String?,
    val meaning: String,
    val examples: List<String>,
    val audioUrl: String?,
    val status: String,
    val setContext: SetContextDto?
)

data class QueueSummaryDto(
    val newCount: Int,
    val reviewCount: Int,
    val totalToday: Int,
    val estimatedMinutes: Int
)

data class LearningQueueResponse(
    val newCards: List<LearningCardDto>,
    val reviewCards: List<LearningCardDto>,
    val summary: QueueSummaryDto
)

interface FlashCardApi{
    @GET("learning/flashcard-test")
    suspend fun getTestSet(): ApiResponse<FlashCardTestDto>

    @GET("learning/sets/{id}/queue")
    suspend fun getSetLearningQueue(
        @Path("id") setId: String
    ): ApiResponse<LearningQueueResponse>
}