package com.minlish.app.data.repository

import com.minlish.app.data.dto.LearningDto
import com.minlish.app.data.dto.ModeDto
import com.minlish.app.data.dto.PracticeModeDto
import com.minlish.app.di.NetworkModule

class PracticeRepository(){
    private val practiceApi = NetworkModule.practiceApi
    suspend fun getPracticeMode(): Result<PracticeModeDto>{
        return try{
            val response= practiceApi.getPracticeMode()
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