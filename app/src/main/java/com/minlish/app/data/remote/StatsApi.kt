package com.minlish.app.data.remote

import com.minlish.app.data.dto.response.ApiResponse
import com.minlish.app.data.dto.response.DashboardStatsDto
import com.minlish.app.data.dto.response.DailyStatsItemDto
import com.minlish.app.data.dto.response.MasteryDistributionDto
import com.minlish.app.data.dto.response.HeatmapItemDto
import retrofit2.http.GET
import retrofit2.http.Query

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
