package com.minlish.app.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

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
    val meanings: List<DictionaryMeaning> = emptyList(),
    val sourceUrl: String? = null
)

interface DictionaryApi {
    @GET("dictionary/lookup")
    suspend fun lookupWord(
        @Query("word") word: String
    ): ApiResponse<DictionaryResponse>
}
