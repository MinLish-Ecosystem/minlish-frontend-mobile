package com.minlish.app.data.repository

import com.minlish.app.data.dto.LearningDto
import com.minlish.app.di.NetworkModule

class LearningRepository(){
    private val learningApi = NetworkModule.learningApi
    suspend fun getLearningSet(id: String): Result<LearningDto>{
        return try{
            val request= learningApi.getLearningSet(id)
            Result.success(request)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}