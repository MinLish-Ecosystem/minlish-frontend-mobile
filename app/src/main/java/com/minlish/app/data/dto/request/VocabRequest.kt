package com.minlish.app.data.dto.request

data class CreateSetRequest(
    val name: String,
    val description: String? = null,
    val category: String? = null,
    val level: String? = null,
    val colorTheme: String? = null,
    val tags: List<String>? = null,
    val isPublic: Boolean = false
)

data class AddWordRequest(
    val word: String,
    val meaning: String,
    val pronunciation: String? = null,
    val partOfSpeech: String? = null,
    val descriptionEN: String? = null,
    val examples: List<String>? = null,
    val synonyms: List<String>? = null,
    val antonyms: List<String>? = null,
    val collocations: List<String>? = null,
    val relatedWords: List<String>? = null,
    val imageUrl: String? = null,
    val audioUrl: String? = null,
    val note: String? = null
)