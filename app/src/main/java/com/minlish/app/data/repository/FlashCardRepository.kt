package com.minlish.app.data.repository

import com.minlish.app.data.dto.FlashCardTestDto
import com.minlish.app.data.dto.LearningDto
import com.minlish.app.di.NetworkModule

class FlashCardRepository(){
    private val flashCardApi = NetworkModule.flashCardApi
    suspend fun getLearningSet(userId: String): Result<FlashCardTestDto> {
        return try{
            val request= flashCardApi.getTestSet(userId)
            Result.success(request)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}