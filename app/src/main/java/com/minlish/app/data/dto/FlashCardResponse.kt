package com.minlish.app.data.dto


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