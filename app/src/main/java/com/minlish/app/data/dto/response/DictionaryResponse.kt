package com.minlish.app.data.dto.response

data class DictionaryDefinition(
    val definition: String,
    val example: String? = null
)

data class DictionaryMeaning(
    val partOfSpeech: String,
    val definitions: List<DictionaryDefinition>
)

data class DictionaryResponse(
    val word: String,
    val phonetic: String? = null,
    val audio: String? = null,
    val meanings: List<DictionaryMeaning>? = null,
    val sourceUrl: String? = null
)