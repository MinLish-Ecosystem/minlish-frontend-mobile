package com.minlish.app.util

import com.minlish.app.data.remote.AddWordRequest
import java.io.InputStream
import java.io.BufferedReader
import java.io.InputStreamReader

object CsvParser {

    /**
     * Tự động phát hiện ký tự phân cách tối ưu nhất (comma, semicolon, colon, tab, hyphen, pipe)
     */
    fun detectDelimiter(lines: List<String>): String {
        val delimiters = listOf(",", ";", "\t", ":", "-", "|")
        val sampleLines = lines.filter { it.isNotBlank() }.take(5)
        if (sampleLines.isEmpty()) return ","

        val scores = delimiters.associateWith { delimiter ->
            val counts = sampleLines.map { line ->
                line.count { it.toString() == delimiter }
            }
            if (counts.all { it > 0 }) counts.minOrNull() ?: 0 else 0
        }

        val best = scores.maxByOrNull { it.value }
        return if (best != null && best.value > 0) best.key else ","
    }

    /**
     * Phân tích một dòng CSV (hỗ trợ ngoặc kép kép làm ký tự thoát "")
     */
    fun parseCsvLine(line: String, delimiter: String): List<String> {
        val result = mutableListOf<String>()
        val currentField = StringBuilder()
        var inQuotes = false
        var i = 0
        val len = line.length
        val delimChar = delimiter.firstOrNull() ?: ','

        while (i < len) {
            val c = line[i]
            if (c == '"') {
                if (inQuotes && i + 1 < len && line[i + 1] == '"') {
                    currentField.append('"')
                    i++
                } else {
                    inQuotes = !inQuotes
                }
            } else if (c == delimChar && !inQuotes) {
                result.add(currentField.toString().trim())
                currentField.setLength(0)
            } else {
                currentField.append(c)
            }
            i++
        }
        result.add(currentField.toString().trim())
        return result
    }

    /**
     * Tự động phân tích cột dựa trên đặc trưng ngôn ngữ (Anh/Việt) để xử lý file không có tiêu đề
     */
    fun detectColumns(rows: List<List<String>>): Pair<Int, Int> {
        if (rows.isEmpty()) return Pair(0, 1)
        val numCols = rows.maxOf { it.size }
        if (numCols < 2) return Pair(0, 0)

        val asciiScores = IntArray(numCols)
        val unicodeScores = IntArray(numCols)

        for (row in rows.take(10)) {
            for (i in row.indices) {
                val text = row[i]
                var asciiCount = 0
                var unicodeAccentCount = 0
                for (char in text) {
                    if (char.code in 65..90 || char.code in 97..122) {
                        asciiCount++
                    } else if (char.code > 127 && "àáảãạâầấẩẫậăằắẳẵặèéẻẽẹêềếểễệìíỉĩịòóỏõọôồốổỗộơờớởỡợùúủũụưừứửữựỳýỷỹỵđĐ".contains(char)) {
                        unicodeAccentCount++
                    }
                }
                asciiScores[i] += asciiCount
                unicodeScores[i] += unicodeAccentCount
            }
        }

        var wordCol = 0
        var meaningCol = 1

        val col0Unicode = unicodeScores.getOrNull(0) ?: 0
        val col1Unicode = unicodeScores.getOrNull(1) ?: 0

        // Cột nào chứa nhiều ký tự tiếng Việt có dấu nhất sẽ là Meaning, cột còn lại là Word
        if (col0Unicode > col1Unicode) {
            wordCol = 1
            meaningCol = 0
        } else {
            wordCol = 0
            meaningCol = 1
        }

        return Pair(wordCol, meaningCol)
    }

    /**
     * Parse thông minh file CSV: tự nhận dạng phân cách, tự nhận dạng cột qua header hoặc ngôn ngữ
     */
    fun parseCsv(inputStream: InputStream): List<AddWordRequest> {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val lines = reader.use { it.readLines() }.filter { it.isNotBlank() }
        if (lines.isEmpty()) return emptyList()

        // 1. Tự động nhận diện ký tự phân cách
        val delimiter = detectDelimiter(lines)

        // 2. Tách dòng
        val parsedRows = lines.map { parseCsvLine(it, delimiter) }
        if (parsedRows.isEmpty()) return emptyList()

        // 3. Khớp cột thông qua Header
        val firstRow = parsedRows.first()
        val headers = firstRow.map { it.lowercase() }

        val wordSynonyms = listOf("word", "term", "vocab", "vocabulary", "từ", "từ vựng", "english", "eng")
        val meaningSynonyms = listOf("meaning", "definition", "translate", "translation", "nghĩa", "định nghĩa", "vietnamese", "vie", "viet")
        val pronSynonyms = listOf("pronunciation", "phonetic", "phiên âm", "ipa")
        val posSynonyms = listOf("part of speech", "pos", "loại từ")
        val noteSynonyms = listOf("note", "ghi chú")
        val exampleSynonyms = listOf("example", "examples", "ví dụ")

        val headerWordIndex = headers.indexOfFirst { h -> wordSynonyms.any { syn -> h.contains(syn) } }
        val headerMeaningIndex = headers.indexOfFirst { h -> meaningSynonyms.any { syn -> h.contains(syn) } }
        val headerPronIndex = headers.indexOfFirst { h -> pronSynonyms.any { syn -> h.contains(syn) } }
        val headerPosIndex = headers.indexOfFirst { h -> posSynonyms.any { syn -> h.contains(syn) } }
        val headerNoteIndex = headers.indexOfFirst { h -> noteSynonyms.any { syn -> h.contains(syn) } }
        val headerExampleIndex = headers.indexOfFirst { h -> exampleSynonyms.any { syn -> h.contains(syn) } }

        val hasHeader = headerWordIndex != -1 && headerMeaningIndex != -1

        val dataRows: List<List<String>>
        val wordCol: Int
        val meaningCol: Int
        val pronCol: Int
        val posCol: Int
        val noteCol: Int
        val exampleCol: Int

        if (hasHeader) {
            dataRows = parsedRows.drop(1)
            wordCol = headerWordIndex
            meaningCol = headerMeaningIndex
            pronCol = headerPronIndex
            posCol = headerPosIndex
            noteCol = headerNoteIndex
            exampleCol = headerExampleIndex
        } else {
            dataRows = parsedRows
            // File không có cấu trúc header -> Nhận diện thông minh theo ngôn ngữ
            val autoCols = detectColumns(dataRows)
            wordCol = autoCols.first
            meaningCol = autoCols.second
            pronCol = -1
            posCol = -1
            noteCol = -1
            exampleCol = -1
        }

        val result = mutableListOf<AddWordRequest>()
        for (row in dataRows) {
            val word = row.getOrNull(wordCol)?.trim() ?: ""
            val meaning = row.getOrNull(meaningCol)?.trim() ?: ""
            if (word.isBlank() || meaning.isBlank()) continue

            val pronunciation = if (pronCol != -1) row.getOrNull(pronCol)?.trim() else null
            val partOfSpeech = if (posCol != -1) row.getOrNull(posCol)?.trim() else null
            val note = if (noteCol != -1) row.getOrNull(noteCol)?.trim() else null
            val examples = if (exampleCol != -1) {
                row.getOrNull(exampleCol)?.trim()?.let { if (it.isNotEmpty()) listOf(it) else null }
            } else null

            result.add(
                AddWordRequest(
                    word = word,
                    meaning = meaning,
                    pronunciation = pronunciation,
                    partOfSpeech = partOfSpeech,
                    examples = examples,
                    note = note
                )
            )
        }
        return result
    }

    /**
     * Xuất danh sách từ vựng thành định dạng chuỗi CSV
     */
    fun toCsvString(words: List<WordResponse>): String {
        val csvBuilder = StringBuilder()
        csvBuilder.append("Word,Meaning,Pronunciation,PartOfSpeech,Example,Note\n")
        
        for (item in words) {
            val wordEsc = escapeCsvField(item.word)
            val meaningEsc = escapeCsvField(item.meaning)
            val pronEsc = escapeCsvField(item.pronunciation ?: "")
            val posEsc = escapeCsvField(item.partOfSpeech ?: "")
            val exampleEsc = escapeCsvField(item.examples.firstOrNull() ?: "")
            val noteEsc = escapeCsvField(item.note ?: "")
            csvBuilder.append("$wordEsc,$meaningEsc,$pronEsc,$posEsc,$exampleEsc,$noteEsc\n")
        }
        return csvBuilder.toString()
    }

    private fun escapeCsvField(field: String): String {
        if (field.contains(",") || field.contains("\"") || field.contains("\n") || field.contains("\r")) {
            return "\"" + field.replace("\"", "\"\"") + "\""
        }
        return field
    }
}
