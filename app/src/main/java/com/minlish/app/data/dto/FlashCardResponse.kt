package com.minlish.app.data.dto


data class FlashCardTestDto(
    val userId: String,
    val flashCardSets: List<FlashcardContentDto>,
)
data class FlashcardContentDto(
    val category: String,
    val word: String,
    val phonetic: String,
    val partOfSpeech: String,
    val definition: String,
    val example: String,
)