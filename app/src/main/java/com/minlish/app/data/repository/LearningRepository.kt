package com.minlish.app.data.repository

import com.minlish.app.data.dto.response.LearningDto
import com.minlish.app.di.NetworkModule

class LearningRepository(){
    private val learningApi = NetworkModule.learningApi
    suspend fun getLearningSet(): Result<LearningDto> {
        return try{
            val response = learningApi.getLearningSet()
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Lỗi không xác định"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}