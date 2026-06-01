package com.minlish.app.data.repository

import com.minlish.app.data.remote.DailyStatsItemDto
import com.minlish.app.data.remote.DashboardStatsDto
import com.minlish.app.data.remote.HeatmapItemDto
import com.minlish.app.data.remote.MasteryDistributionDto
import com.minlish.app.di.NetworkModule

class StatsRepository {
    private val statsApi = NetworkModule.statsApi

    suspend fun getDashboardStats(): Result<DashboardStatsDto> = runCatching {
        statsApi.getDashboardStats().data ?: throw Exception("Dashboard data is null")
    }

    suspend fun getWeeklyStats(): Result<List<DailyStatsItemDto>> = runCatching {
        statsApi.getDailyStats(7).data ?: throw Exception("Weekly data is null")
    }

    suspend fun getMasteryDistribution(): Result<MasteryDistributionDto> = runCatching {
        statsApi.getMasteryDistribution().data ?: throw Exception("Mastery data is null")
    }

    suspend fun getHeatmap(): Result<List<HeatmapItemDto>> = runCatching {
        statsApi.getHeatmap().data ?: throw Exception("Heatmap data is null")
    }
}
