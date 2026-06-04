package com.minlish.app.util

import com.minlish.app.data.remote.AddWordRequest
import com.minlish.app.data.remote.WordResponse
import java.io.InputStream
import java.io.BufferedReader
import java.io.InputStreamReader

object CsvParser {

    /**
     * Tách dòng CSV hỗ trợ dấu ngoặc kép kép làm ký tự thoát
     */
    fun parseCsvLine(line: String): List<String> {
        val result = mutableListOf<String>()
        val currentField = StringBuilder()
        var inQuotes = false
        var i = 0
        val len = line.length

        while (i < len) {
            val c = line[i]
            if (c == '"') {
                if (inQuotes && i + 1 < len && line[i + 1] == '"') {
                    currentField.append('"')
                    i++
                } else {
                    inQuotes = !inQuotes
                }
            } else if (c == ',' && !inQuotes) {
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
     * LUỒNG 1: Parse file CSV native (đầy đủ cấu trúc) của MinLish
     * Định dạng cột: Word,Meaning,Pronunciation,PartOfSpeech,Example,Note
     */
    fun parseNativeCsv(inputStream: InputStream): List<AddWordRequest> {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val lines = reader.use { it.readLines() }.filter { it.isNotBlank() }
        if (lines.isEmpty()) return emptyList()

        // Bỏ dòng tiêu đề (Header)
        val dataLines = lines.drop(1)
        val list = mutableListOf<AddWordRequest>()

        for (line in dataLines) {
            val fields = parseCsvLine(line)
            val word = fields.getOrNull(0) ?: ""
            val meaning = fields.getOrNull(1) ?: ""
            if (word.isBlank() || meaning.isBlank()) continue

            val pronunciation = fields.getOrNull(2)?.takeIf { it.isNotBlank() }
            val partOfSpeech = fields.getOrNull(3)?.takeIf { it.isNotBlank() }
            val example = fields.getOrNull(4)?.takeIf { it.isNotBlank() }
            val note = fields.getOrNull(5)?.takeIf { it.isNotBlank() }

            list.add(
                AddWordRequest(
                    word = word,
                    meaning = meaning,
                    pronunciation = pronunciation,
                    partOfSpeech = partOfSpeech,
                    examples = example?.let { listOf(it) },
                    note = note
                )
            )
        }
        return list
    }

    /**
     * LUỒNG 2: Trích xuất danh sách từ tiếng Anh từ một file bất kỳ (TXT, CSV đơn giản)
     * Làm sạch dấu cách, lọc bỏ các dòng trống hoặc ký tự lạ để lấy danh sách từ thuần túy
     */
    fun extractWordsList(inputStream: InputStream): List<String> {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val rawText = reader.use { it.readText() }
        return extractWordsList(rawText)
    }

    /**
     * Trích xuất danh sách từ tiếng Anh từ văn bản dán tự do
     */
    fun extractWordsList(rawText: String): List<String> {
        if (rawText.isBlank()) return emptyList()
        
        // Tách theo dòng hoặc các ký tự phân tách phổ biến (dấu phẩy, chấm phẩy, tab...)
        val rawLines = rawText.split(Regex("[\n\r,;\t|]+"))
        val cleanWords = mutableListOf<String>()

        for (line in rawLines) {
            // Loại bỏ khoảng trắng thừa, chỉ lấy các dòng chứa chữ cái tiếng Anh
            val word = line.trim()
            
            // Regex kiểm tra xem chuỗi có chứa từ tiếng Anh hợp lệ không (chấp nhận cả cụm từ có dấu cách hoặc gạch ngang)
            if (word.isNotEmpty() && word.matches(Regex("^[a-zA-Z\\s\\-]+$"))) {
                cleanWords.add(word)
            }
        }
        
        return cleanWords.distinct()
    }

    /**
     * Xuất danh sách từ vựng hiện tại thành định dạng chuỗi CSV chuẩn MinLish
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
