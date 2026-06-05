package com.minlish.app.data.dto.response

data class VocabSetProgress(
    val masteredCount: Int,
    val masteredPct: Int,
    val learningCount: Int,
    val newCount: Int,
    val dueToday: Int,
    val lastStudied: String?
)

data class VocabSetResponse(
    val id: String,
    val name: String,
    val description: String?,
    val category: String,
    val level: String,
    val colorTheme: String,
    val tags: List<String>,
    val isPublic: Boolean,
    val totalWords: Int,
    val learnerCount: Int,
    val clonedFrom: String?,
    val createdAt: String,
    val updatedAt: String,
    val progress: VocabSetProgress? = null
)

data class WordResponse(
    val id: String,
    val setId: String,
    val word: String,
    val pronunciation: String?,
    val partOfSpeech: String?,
    val meaning: String,
    val descriptionEN: String?,
    val examples: List<String>,
    val synonyms: List<String>,
    val antonyms: List<String>,
    val collocations: List<String>,
    val relatedWords: List<String> = emptyList(),
    val note: String?,
    val imageUrl: String?,
    val audioUrl: String?,
    val status: String? = null,
    val masteryPct: Int? = null
)

data class PaginatedData<T>(
    val data: List<T>,
    val pagination: Pagination
)

data class Pagination(
    val page: Int,
    val limit: Int,
    val total: Int,
    val totalPages: Int
)