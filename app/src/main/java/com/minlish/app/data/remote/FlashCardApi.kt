package com.minlish.app.data.remote
import com.minlish.app.data.dto.BatchSubmitReviewDto
import com.minlish.app.data.dto.FlashCardTestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.*

data class SetContextDto(
    val setId: String,
    val setName: String?,
    val colorTheme: String?
)

data class LearningCardDto(
    val id: String,
    val setId: String,
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
    @POST("learning/submit-review-batch")
    suspend fun submitBatchReview(@Body request: BatchSubmitReviewDto): ApiResponse<Unit>

    @GET("learning/sets/{id}/queue")
    suspend fun getSetLearningQueue(
        @Path("id") setId: String
    ): ApiResponse<LearningQueueResponse>
}