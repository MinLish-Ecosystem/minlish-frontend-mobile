package com.minlish.app.data.repository

import com.minlish.app.data.dto.LearningDto
import com.minlish.app.data.dto.ModeDto
import com.minlish.app.data.dto.PracticeModeDto
import com.minlish.app.di.NetworkModule

class PracticeRepository(){
    private val practiceApi = NetworkModule.practiceApi
    suspend fun getPracticeMode(id: String): Result<PracticeModeDto>{
        return try{
            val request= practiceApi.getPracticeMode(id)
            Result.success(request)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}