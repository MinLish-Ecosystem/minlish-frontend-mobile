package com.minlish.app.util

import com.minlish.app.data.dto.request.AddWordRequest
import com.minlish.app.data.dto.response.DictionaryMeaning
import com.minlish.app.data.repository.VocabRepository
import com.minlish.app.data.repository.VocabResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object VocabularyImporter {
    private val repository = VocabRepository()

    data class ImportProgress(
        val current: Int,
        val total: Int
    )

    data class ImportResult(
        val successCount: Int,
        val failedCount: Int,
        val failedWords: List<String>
    )

    suspend fun importWords(
        setId: String,
        words: List<AddWordRequest>,
        onProgress: (ImportProgress) -> Unit
    ): ImportResult = withContext(Dispatchers.IO) {
        var successCount = 0
        var failedCount = 0
        val failedWords = mutableListOf<String>()
        val total = words.size
        for ((index, request) in words.withIndex()) {
            withContext(Dispatchers.Main) {
                onProgress(ImportProgress(index + 1, total))
            }
            when (val result = repository.addWord(setId, request)) {
                is VocabResult.Success -> {
                    successCount++
                }
                is VocabResult.Error -> {
                    failedCount++
                    failedWords.add("${request.word}: ${result.message}")
                }
            }
        }
        ImportResult(
            successCount = successCount,
            failedCount = failedCount,
            failedWords = failedWords
        )
    }

    data class LookupResult(
        val successWords: List<AddWordRequest>,
        val failedWords: List<String>
    )

    suspend fun lookupWords(
        words: List<String>,
        onProgress: ((ImportProgress) -> Unit)?=null
    ): LookupResult = withContext(Dispatchers.IO) {
        val successWords = mutableListOf<AddWordRequest>()
        val failedWords = mutableListOf<String>()
        val total = words.size
        for ((index, word) in words.withIndex()){
            withContext(Dispatchers.Main){
                onProgress?.invoke(ImportProgress(index+1, total))
            }
            when (val lookupResult = repository.lookupWord(word)){
                is VocabResult.Success -> {
                    val dict = lookupResult.data
                    val firstMeaning: DictionaryMeaning?=dict.meanings?.firstOrNull()
                    val meaning = firstMeaning?.definitions?.firstOrNull()?.definition?:""
                    val examples = firstMeaning?.definitions?.mapNotNull{it.example}?.take(3)?:emptyList()
                    if (meaning.isNotBlank()){
                        successWords.add(
                            AddWordRequest(
                                word = dict.word,
                                meaning = meaning,
                                pronunciation = dict.phonetic,
                                partOfSpeech = firstMeaning?.partOfSpeech,
                                examples = examples.ifEmpty{null},
                                audioUrl = dict.audio
                            )
                        )
                    } else {
                        failedWords.add(word)
                    }
                }
                is VocabResult.Error -> {
                    failedWords.add(word)
                }
            }
        }
        LookupResult(successWords, failedWords)
    }
}
