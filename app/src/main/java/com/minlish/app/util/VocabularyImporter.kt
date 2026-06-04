package com.minlish.app.util

import com.minlish.app.data.remote.AddWordRequest
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

    /**
     * Nạp danh sách từ vựng lên server một cách tuần tự bất đồng bộ trên luồng IO
     */
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
}
