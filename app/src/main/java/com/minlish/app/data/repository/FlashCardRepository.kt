package com.minlish.app.data.repository

import com.minlish.app.data.dto.BatchSubmitReviewDto
import com.minlish.app.data.dto.FlashCardTestDto
import com.minlish.app.data.dto.LearningDto
import com.minlish.app.data.dto.ReviewItemDto
import com.minlish.app.data.remote.LearningQueueResponse
import com.minlish.app.di.NetworkModule

class FlashCardRepository(){
    private val flashCardApi = NetworkModule.flashCardApi
    suspend fun getLearningSet(): Result<FlashCardTestDto> {
        return try{
            val response= flashCardApi.getTestSet()
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Lỗi không xác định"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
    suspend fun submitBatchReview(reviews: List<ReviewItemDto>): Result<Unit> {
        return try {
            val request = flashCardApi.submitBatchReview(
                BatchSubmitReviewDto(reviews = reviews)
            )
            if (request.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(request.message ?: "Lỗi không xác định"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSetLearningQueue(setId: String): Result<LearningQueueResponse> {
        return try {
            val response = flashCardApi.getSetLearningQueue(setId)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Lỗi không xác định"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}