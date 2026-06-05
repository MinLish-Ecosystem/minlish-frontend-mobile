package com.minlish.app.presentation.screens.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minlish.app.data.repository.StatsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AnalyticsViewModel : ViewModel() {
    private val repository = StatsRepository()
    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    init {
        loadAnalytics()
    }

    private fun loadAnalytics() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val dashboardDeferred = async { repository.getDashboardStats() }
                val weeklyDeferred = async { repository.getWeeklyStats() }
                val masteryDeferred = async { repository.getMasteryDistribution() }
                val heatmapDeferred = async { repository.getHeatmap() }
                val dashboardRes = dashboardDeferred.await()
                val weeklyRes = weeklyDeferred.await()
                val masteryRes = masteryDeferred.await()
                val heatmapRes = heatmapDeferred.await()
                if (dashboardRes.isSuccess && weeklyRes.isSuccess && masteryRes.isSuccess && heatmapRes.isSuccess) {
                    val dashboard = dashboardRes.getOrThrow()
                    val weeklyList = weeklyRes.getOrThrow()
                    val mastery = masteryRes.getOrThrow()
                    val heatmapList = heatmapRes.getOrThrow()
                    val wordsThisWeek = weeklyList.sumOf { it.newWordsLearned }
                    val sdfInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val sdfOutput = SimpleDateFormat("E", Locale.getDefault())
                    val weeklyMinutes = weeklyList.map { item ->
                        val dayLabel = try {
                            val parsedDate = sdfInput.parse(item.date)
                            val formatted = sdfOutput.format(parsedDate!!)
                            formatted.substring(0, 1).uppercase()
                        } catch (e: Exception) {
                            "M"
                        }
                        dayLabel to (item.timeSpent / 60)
                    }
                    val totalWords = mastery.total.toFloat().coerceAtLeast(1f)
                    val beginnerCount = mastery.new + mastery.learning
                    val intermediateCount = mastery.review
                    val advancedCount = mastery.mastered
                    val masteryBeginner = MasteryLevel("Beginner", beginnerCount, beginnerCount / totalWords)
                    val masteryIntermediate = MasteryLevel("Intermediate", intermediateCount, intermediateCount / totalWords)
                    val masteryAdvanced = MasteryLevel("Advanced", advancedCount, advancedCount / totalWords)
                    val heatmapMap = heatmapList.associate { it.date to it.count }
                    val maxCount = heatmapList.maxOfOrNull { it.count }?.coerceAtLeast(1) ?: 1
                    val calendar = Calendar.getInstance()
                    val activityList = mutableListOf<Float>()
                    calendar.add(Calendar.DAY_OF_YEAR, -34)
                    val sdfHeatmap = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    for (i in 0 until 35) {
                        val dateStr = sdfHeatmap.format(calendar.time)
                        val count = heatmapMap[dateStr] ?: 0
                        val intensity = count.toFloat() / maxCount.toFloat()
                        activityList.add(intensity.coerceIn(0f, 1f))
                        calendar.add(Calendar.DAY_OF_YEAR, 1)
                    }
                    _uiState.update {
                        it.copy(
                            currentStreak = dashboard.streak.current,
                            timeSpentHours = dashboard.timeSpent.totalHours,
                            wordsMastered = dashboard.masteredWords,
                            wordsThisWeek = wordsThisWeek,
                            weeklyMinutes = weeklyMinutes,
                            masteryBeginner = masteryBeginner,
                            masteryIntermediate = masteryIntermediate,
                            masteryAdvanced = masteryAdvanced,
                            activityMap = activityList,
                            isLoading = false
                        )
                    }
                } else {
                    val errorMsg = listOf(
                        dashboardRes.exceptionOrNull()?.message,
                        weeklyRes.exceptionOrNull()?.message,
                        masteryRes.exceptionOrNull()?.message,
                        heatmapRes.exceptionOrNull()?.message
                    ).filterNotNull().joinToString(", ")
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = if (errorMsg.isNotEmpty()) errorMsg else "Failed to load stats")
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "Failed to fetch analytics")
                }
            }
        }
    }
    fun refresh() = loadAnalytics()
}