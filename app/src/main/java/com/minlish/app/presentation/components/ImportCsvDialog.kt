package com.minlish.app.presentation.components

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.app.data.remote.AddWordRequest
import com.minlish.app.util.CsvParser
import com.minlish.app.util.VocabularyImporter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportCsvDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    setId: String? = null, // Nếu truyền: sẽ gọi API nạp trực tiếp qua backend
    onImportCompleted: () -> Unit = {}, // Callback sau khi import thành công
    onWordsParsed: ((List<AddWordRequest>) -> Unit)? = null // Callback trả về danh sách để giao diện ngoài tự nạp (Tạo bộ từ mới)
) {
    if (!isOpen) return

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var selectedFileName by remember { mutableStateOf("") }
    var parsedWords by remember { mutableStateOf<List<AddWordRequest>>(emptyList()) }
    var showPreview by remember { mutableStateOf(false) }

    var importProgress by remember { mutableStateOf<VocabularyImporter.ImportProgress?>(null) }
    var importResult by remember { mutableStateOf<VocabularyImporter.ImportResult?>(null) }

    fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (index != -1) result = cursor.getString(index)
                }
            } finally {
                cursor?.close()
            }
        }
        return result ?: uri.path?.substringAfterLast('/') ?: "vocab.csv"
    }

    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectedFileName = getFileName(uri)
            try {
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    parsedWords = CsvParser.parseCsv(stream)
                }
                showPreview = true
            } catch (e: Exception) {
                Toast.makeText(context, "Lỗi đọc file: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Nhập Từ Vựng Từ File CSV",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            if (!showPreview) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                        .clickable { fileLauncher.launch("*/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(40.dp))
                        Text("Bấm để chọn file CSV từ máy", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                        Text(
                            text = "Định dạng cột: Word, Meaning, Pronunciation, PartOfSpeech, Example, Note",
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Text(text = "Đọc thành công ${parsedWords.size} từ. Xem trước 5 từ đầu:", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 180.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(parsedWords.take(5)) { item ->
                        Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(6.dp)).padding(8.dp)) {
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text(item.word, fontWeight = FontWeight.Bold)
                                Text(item.partOfSpeech ?: "", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                            }
                            Text(item.meaning, fontSize = 13.sp)
                            if (!item.pronunciation.isNullOrBlank()) {
                                Text(item.pronunciation!!, fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    }
                }

                // Nút Đảo cột nếu nhận dạng ngôn ngữ nhầm
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            parsedWords = parsedWords.map {
                                AddWordRequest(
                                    word = it.meaning,
                                    meaning = it.word,
                                    pronunciation = it.pronunciation,
                                    partOfSpeech = it.partOfSpeech,
                                    examples = it.examples,
                                    note = it.note
                                )
                            }
                        }
                    ) {
                        Icon(Icons.Default.SwapHoriz, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Đảo cột Từ / Nghĩa", fontSize = 12.sp)
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = { showPreview = false }, modifier = Modifier.weight(1f).height(48.dp), shape = RoundedCornerShape(12.dp)) {
                        Text("Hủy")
                    }
                    Button(
                        onClick = {
                            if (setId != null) {
                                coroutineScope.launch {
                                    importProgress = VocabularyImporter.ImportProgress(0, parsedWords.size)
                                    val result = VocabularyImporter.importWords(
                                        setId = setId,
                                        words = parsedWords,
                                        onProgress = { importProgress = it }
                                    )
                                    importProgress = null
                                    importResult = result
                                    onImportCompleted()
                                }
                            } else if (onWordsParsed != null) {
                                onWordsParsed(parsedWords)
                                onDismiss()
                            }
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Bắt đầu nạp", fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (importProgress != null) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {},
            title = { Text("Đang nhập từ vựng...", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                val progress = importProgress!!
                val pct = if (progress.second > 0) (progress.first * 100) / progress.second else 0
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Đang nạp: ${progress.first}/${progress.second} từ ($pct%)", fontSize = 14.sp)
                    LinearProgressIndicator(
                        progress = { if (progress.second > 0) progress.first.toFloat() / progress.second.toFloat() else 0f },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp))
                    )
                }
            }
        )
    }

    if (importResult != null) {
        val result = importResult!!
        AlertDialog(
            onDismissRequest = {
                importResult = null
                onDismiss()
            },
            confirmButton = { Button(onClick = { importResult = null; onDismiss() }) { Text("Đóng") } },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(
                        imageVector = if (result.failedCount == 0) Icons.Default.CheckCircle else Icons.Default.Warning,
                        contentDescription = null,
                        tint = if (result.failedCount == 0) Color(0xFF10B981) else Color(0xFFF59E0B)
                    )
                    Text("Kết Quả Nhập Từ", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Thành công: ${result.successCount} từ", color = Color(0xFF065F46), fontWeight = FontWeight.Medium)
                    if (result.failedCount > 0) {
                        Text("Thất bại: ${result.failedCount} từ (vui lòng kiểm tra kết nối mạng)", color = Color(0xFF92400E), fontWeight = FontWeight.Medium)
                        LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 100.dp)) {
                            items(result.failedWords) { errWord ->
                                Text("• $errWord", fontSize = 12.sp, color = Color.Red)
                            }
                        }
                    }
                }
            }
        )
    }
}
