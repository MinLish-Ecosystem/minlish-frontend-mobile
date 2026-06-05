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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.app.data.dto.request.AddWordRequest
import com.minlish.app.data.repository.VocabRepository
import com.minlish.app.util.CsvParser
import com.minlish.app.util.VocabularyImporter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportCsvDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    setId: String? = null,
    onImportCompleted: () -> Unit = {},
    onWordsParsed: ((List<AddWordRequest>) -> Unit)? = null
) {
    if (!isOpen) return
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val repository = remember { VocabRepository() }
    var importSourceTab by remember { mutableStateOf(0) }
    var importMode by remember { mutableStateOf(0) }
    var selectedFileName by remember { mutableStateOf("") }
    var rawTextData by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var parsedWords by remember { mutableStateOf<List<AddWordRequest>>(emptyList()) }
    var failedLookupWords by remember { mutableStateOf<List<String>>(emptyList()) }
    var showPreview by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }
    var importProgress by remember { mutableStateOf<VocabularyImporter.ImportProgress?>(null) }
    var importResult by remember { mutableStateOf<VocabularyImporter.ImportResult?>(null) }

    LaunchedEffect(isOpen) {
        if (isOpen) {
            importSourceTab = 0
            importMode = 0
            selectedFileName = ""
            rawTextData = ""
            selectedFileUri = null
            parsedWords = emptyList()
            failedLookupWords = emptyList()
            showPreview = false
            isProcessing = false
            importProgress = null
            importResult = null
        }
    }

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
            selectedFileUri = uri
            selectedFileName = getFileName(uri)
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
                text = "Nhập Từ Vựng Vào Hệ Thống",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            if (!showPreview) {
                TabRow(selectedTabIndex = importSourceTab) {
                    Tab(selected = importSourceTab == 0, onClick = { importSourceTab = 0 }, text = { Text("Chọn Tệp Tin") })
                    Tab(selected = importSourceTab == 1, onClick = { importSourceTab = 1 }, text = { Text("Dán Văn Bản") })
                }
                if (importSourceTab == 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                            .clickable { fileLauncher.launch("*/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
                            Text(
                                text = if (selectedFileUri != null) selectedFileName else "Bấm để chọn file CSV hoặc TXT",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 13.sp
                            )
                        }
                    }
                } else {
                    OutlinedTextField(
                        value = rawTextData,
                        onValueChange = { rawTextData = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Dán danh sách từ của bạn vào đây...", fontSize = 13.sp) },
                        shape = RoundedCornerShape(12.dp),
                        minLines = 3,
                        maxLines = 4
                    )
                }

                Text("Chọn cấu trúc dữ liệu tệp nạp vào:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = if (importMode == 0) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).clickable { importMode = 0 }
                    ) {
                        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("MinLish Native", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("File CSV xuất từ App", fontSize = 10.sp, color = Color.Gray, textAlign = TextAlign.Center)
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = if (importMode == 1) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).clickable { importMode = 1 }
                    ) {
                        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Danh Sách Từ Thô", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Chỉ chứa từ tiếng Anh", fontSize = 10.sp, color = Color.Gray, textAlign = TextAlign.Center)
                        }
                    }
                }

                Button(
                    onClick = {
                        if (importSourceTab == 0 && selectedFileUri == null) {
                            Toast.makeText(context, "Vui lòng chọn file!", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (importSourceTab == 1 && rawTextData.isBlank()) {
                            Toast.makeText(context, "Vui lòng dán văn bản từ vựng!", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        isProcessing = true
                        coroutineScope.launch{
                            try{
                                val contentStream = if (importSourceTab == 0) {
                                    context.contentResolver.openInputStream(selectedFileUri!!)
                                } else null
                                if (importMode == 0) {
                                    if (contentStream != null){
                                        parsedWords = CsvParser.parseNativeCsv(contentStream)
                                    } else {
                                        Toast.makeText(context, "Mau native yeu cau chon tep csv xuat ra tu he thong", Toast.LENGTH_LONG).show()
                                        isProcessing = false
                                        return@launch
                                    }
                                    showPreview = true
                                }
                                else {
                                    val englishWords = if (contentStream != null) {
                                        CsvParser.extractWordsList(contentStream)
                                    } else {
                                        CsvParser.extractWordsList(rawTextData)
                                    }
                                    if (englishWords.isEmpty()){
                                        throw Exception("Khong tim thay tu tieng Anh hop le nao trong tep")
                                    }
                                    val lookUp = VocabularyImporter.lookupWords(englishWords)
                                    if (lookUp.successWords.isEmpty()){
                                        throw Exception("Khong tra duoc nghia cho bat ky tu nao, vui long kiem tra ket noi mang hoac danh sach tu")
                                    }
                                    parsedWords = lookUp.successWords
                                    failedLookupWords = lookUp.failedWords
                                    showPreview = true
                                }
                            }
                            catch (e: Exception) {
                                Toast.makeText(context, "Loi phan tich: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                            }
                            finally{
                                isProcessing = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Xem Trước & Tiếp Tục", fontWeight = FontWeight.Bold)
                }
            } else {
                Text(text = "Đọc thành công ${parsedWords.size} từ vựng. Xem trước 5 từ đầu:", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 240.dp)
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
                        }
                    }
                }

                if (failedLookupWords.isNotEmpty()) {
                    Text(
                        text = "⚠️ Bỏ qua ${failedLookupWords.size} từ không tra được: ${failedLookupWords.joinToString(", ")}",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                    )
                }

                if (importMode == 0) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
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
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = { showPreview = false }, modifier = Modifier.weight(1f).height(48.dp), shape = RoundedCornerShape(12.dp)) { Text("Quay Lại") }
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
                        Text("Nạp Vào Bộ Từ", fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (isProcessing) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {},
            title = { Text("Đang kết nối API phân dịch...", fontWeight = FontWeight.Bold, fontSize = 15.sp) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Đang tạo nghĩa và câu ví dụ từ danh sách...", fontSize = 12.sp, textAlign = TextAlign.Center)
                }
            }
        )
    }

    if (importProgress != null) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {},
            title = { Text("Đang nạp từ vựng...", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                val progress = importProgress!!
                val pct = if (progress.total > 0) (progress.current * 100) / progress.total else 0
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Tiến độ: ${progress.current}/${progress.total} từ ($pct%)", fontSize = 14.sp)
                    LinearProgressIndicator(
                        progress = { if (progress.total > 0) progress.current.toFloat() / progress.total.toFloat() else 0f },
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

