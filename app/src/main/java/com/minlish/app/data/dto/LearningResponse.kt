package com.minlish.app.data.dto

data class LearningDto(
    val userId: String,
    val vocabSets: List<VocabSetDto>,
    val newWords: Int,
    val reviewsDue: Int,
)

data class VocabSetDto(
    val id: String,
    val title: String,
    val wordCount: Int,
    val icon: String,
    val isDueToday: Boolean
)
// coi lại