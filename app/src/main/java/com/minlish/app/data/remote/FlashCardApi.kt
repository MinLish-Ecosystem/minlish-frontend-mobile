package com.minlish.app.data.remote
import com.minlish.app.data.dto.request.BatchSubmitReviewDto
import com.minlish.app.data.dto.response.ApiResponse
import com.minlish.app.data.dto.response.FlashCardTestDto
import com.minlish.app.data.dto.response.LearningQueueResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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