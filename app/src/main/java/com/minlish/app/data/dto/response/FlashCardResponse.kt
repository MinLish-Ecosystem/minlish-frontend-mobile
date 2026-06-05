package com.minlish.app.data.dto.response


data class FlashCardTestDto(
    val userId: String,
    val flashCardSets: List<FlashcardContentDto>,
)
data class FlashcardContentDto(
    val id: String,
    val setId: String,
    val category: String,
    val word: String,
    val phonetic: String,
    val partOfSpeech: String,
    val definition: String,
    val example: String,
    val audioUrl: String = ""
)

data class SetContextDto(
    val setId: String,
    val setName: String?,
    val colorTheme: String?
)

data class LearningCardDto(
    val id: String,
    val setId: String,
    val word: String,
    val pronunciation: String?,
    val partOfSpeech: String?,
    val meaning: String,
    val examples: List<String>,
    val audioUrl: String?,
    val status: String,
    val setContext: SetContextDto?
)

data class QueueSummaryDto(
    val newCount: Int,
    val reviewCount: Int,
    val totalToday: Int,
    val estimatedMinutes: Int
)

data class LearningQueueResponse(
    val newCards: List<LearningCardDto>,
    val reviewCards: List<LearningCardDto>,
    val summary: QueueSummaryDto
)