package com.minlish.app.data.remote


import com.minlish.app.data.dto.BatchSubmitReviewDto
import com.minlish.app.data.dto.FlashCardTestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FlashCardApi{
    @GET("learning/flashcard-test")
    suspend fun getTestSet(): ApiResponse<FlashCardTestDto>
    @POST("learning/submit-review-batch")
    suspend fun submitBatchReview(@Body request: BatchSubmitReviewDto): ApiResponse<Unit>
}