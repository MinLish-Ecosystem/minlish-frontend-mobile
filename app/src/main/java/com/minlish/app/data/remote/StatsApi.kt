package com.minlish.app.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

data class StreakDto(
    val current: Int,
    val longest: Int
)

data class TimeSpentDto(
    val totalSeconds: Long,
    val totalHours: Float
)

data class CurrentLevelDto(
    val estimated: String,
    val confidence: Int
)

data class TodayStatsDto(
    val newLearned: Int,
    val reviewed: Int,
    val accuracy: Int,
    val dueCount: Int
)

data class DashboardStatsDto(
    val streak: StreakDto,
    val totalWordsLearned: Int,
    val masteredWords: Int,
    val totalReviews: Int,
    val overallAccuracy: Int,
    val timeSpent: TimeSpentDto,
    val currentLevel: CurrentLevelDto,
    val todayStats: TodayStatsDto
)

data class DailyStatsItemDto(
    val date: String,
    val newWordsLearned: Int,
    val wordsReviewed: Int,
    val correctAnswers: Int,
    val totalAnswers: Int,
    val accuracy: Int,
    val timeSpent: Int
)

data class MasteryDistributionDto(
    val new: Int,
    val learning: Int,
    val review: Int,
    val mastered: Int,
    val total: Int
)

data class HeatmapItemDto(
    val date: String,
    val count: Int
)

interface StatsApi {
    @GET("stats/dashboard")
    suspend fun getDashboardStats(): ApiResponse<DashboardStatsDto>

    @GET("stats/daily")
    suspend fun getDailyStats(@Query("days") days: Int): ApiResponse<List<DailyStatsItemDto>>

    @GET("stats/mastery-distribution")
    suspend fun getMasteryDistribution(): ApiResponse<MasteryDistributionDto>

    @GET("stats/heatmap")
    suspend fun getHeatmap(): ApiResponse<List<HeatmapItemDto>>
}
