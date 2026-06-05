package com.minlish.app.presentation.screens.vocab

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.app.ui.theme.MinLishMobileTheme
import com.minlish.app.data.dto.request.AddWordRequest
import com.minlish.app.presentation.components.ImportCsvDialog

private object CreateSetColors {
    val Background      = Color(0xFFF9FAFB)
    val Surface         = Color(0xFFFFFFFF)
    val SurfaceVariant  = Color(0xFFF3F4F6)
    val OnSurface       = Color(0xFF111827)
    val OnSurfaceVariant= Color(0xFF4B5563)
    val Outline         = Color(0xFFD1D5DB)
    val OutlineVariant  = Color(0xFFE5E7EB)
    val Primary         = Color(0xFF4F46E5)         
    val PrimaryContainer= Color(0xFFE0E7FF)           
    val OnPrimary       = Color(0xFFFFFFFF)
    val OnPrimaryContainer = Color(0xFF312E81)        
    val Error           = Color(0xFFEF4444)
    val ErrorContainer  = Color(0xFFFEE2E2)
    val GradientStart   = Color(0xFF667EEA)
    val GradientEnd     = Color(0xFF764BA2)
}

data class WordEntry(
    val id: Int,
    var term: String = "",
    var definition: String = ""
)

private val CATEGORIES = listOf("Academic", "Business", "Travel", "Casual", "General", "Other")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNewSetScreen(
    onBackClick: () -> Unit = {},
    onCreateClick: (title: String, description: String, category: String, isPublic: Boolean, words: List<WordEntry>) -> Unit = { _, _, _, _, _ -> }
) {
    var setName         by remember { mutableStateOf("") }
    var description     by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Business") }  
    var isPublic        by remember { mutableStateOf(true) }
    var words           by remember { mutableStateOf(listOf<WordEntry>()) }
    var wordIdCounter   by remember { mutableIntStateOf(0) }
    var showImportCsvDialog by remember { mutableStateOf(false) }
    val isNameError = setName.isNotEmpty() && setName.length < 2
    val canCreate   = setName.length >= 2

    Scaffold(
        containerColor = CreateSetColors.Background,
        topBar = {
            CreateSetTopBar(onBackClick = onBackClick)
        },
        bottomBar = {
            CreateSetBottomBar(
                canCreate = canCreate,
                onClick = {
                    if (canCreate) {
                        onCreateClick(setName, description, selectedCategory, isPublic, words)
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {
                FormSection(
                    setName         = setName,
                    onSetNameChange = { setName = it },
                    isNameError     = isNameError,
                    description     = description,
                    onDescChange    = { description = it },
                    selectedCategory = selectedCategory,
                    onCategorySelect = { selectedCategory = it },
                    isPublic        = isPublic,
                    onPublicToggle  = { isPublic = it }
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Vocabulary List",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CreateSetColors.OnSurface
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { showImportCsvDialog = true },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Upload,
                                contentDescription = null,
                                tint = CreateSetColors.Primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Import CSV",
                                color = CreateSetColors.Primary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = CreateSetColors.SurfaceVariant
                        ) {
                            Text(
                                text = "${words.size} items",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CreateSetColors.OnSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            if (words.isEmpty()) {
                item {
                    EmptyWordState(
                        onAddWord = {
                            words = words + WordEntry(id = ++wordIdCounter)
                        }
                    )
                }
            } else {
                itemsIndexed(words, key = { _, w -> w.id }) { index, word ->
                    WordEntryCard(
                        index = index,
                        word = word,
                        onTermChange = { newTerm ->
                            words = words.map { if (it.id == word.id) it.copy(term = newTerm) else it }
                        },
                        onDefinitionChange = { newDef ->
                            words = words.map { if (it.id == word.id) it.copy(definition = newDef) else it }
                        },
                        onDelete = {
                            words = words.filter { it.id != word.id }
                        }
                    )
                }
                item {
                    OutlinedButton(
                        onClick = { words = words + WordEntry(id = ++wordIdCounter) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            CreateSetColors.OutlineVariant
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = CreateSetColors.Primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Add Word",
                            color = CreateSetColors.Primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }

    ImportCsvDialog(
        isOpen = showImportCsvDialog,
        onDismiss = { showImportCsvDialog = false },
        setId = null,
        onWordsParsed = { parsedList: List<AddWordRequest> ->
            val newEntries = parsedList.map { req ->
                WordEntry(
                    id = ++wordIdCounter,
                    term = req.word,
                    definition = req.meaning
                )
            }
            words = words + newEntries
            showImportCsvDialog = false
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateSetTopBar(onBackClick: () -> Unit) {
    Surface(
        color = CreateSetColors.Surface.copy(alpha = 0.9f),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = CreateSetColors.OnSurfaceVariant
                )
            }
            Text(
                text = "Create New Set",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CreateSetColors.OnSurface,
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.size(48.dp))
        }
    }
}

@Composable
private fun CreateSetBottomBar(canCreate: Boolean, onClick: () -> Unit) {
    Surface(
        color = CreateSetColors.Surface.copy(alpha = 0.95f),
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = if (canCreate)
                            Brush.linearGradient(
                                listOf(CreateSetColors.GradientStart, CreateSetColors.GradientEnd)
                            )
                        else
                            Brush.linearGradient(
                                listOf(Color(0xFFB0B0B0), Color(0xFF909090))
                            )
                    )
                    .clickable(enabled = canCreate) { onClick() }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Create Set",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FormSection(
    setName: String,
    onSetNameChange: (String) -> Unit,
    isNameError: Boolean,
    description: String,
    onDescChange: (String) -> Unit,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    isPublic: Boolean,
    onPublicToggle: (Boolean) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = CreateSetColors.Surface,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Set Name",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CreateSetColors.OnSurface
                )
                OutlinedTextField(
                    value = setName,
                    onValueChange = onSetNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "e.g., Business English 101",
                            color = CreateSetColors.OnSurfaceVariant.copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                    },
                    isError = isNameError,
                    supportingText = if (isNameError) {
                        { Text("Tên phải có ít nhất 2 ký tự", color = CreateSetColors.Error, fontSize = 12.sp) }
                    } else null,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CreateSetColors.Primary,
                        unfocusedBorderColor = CreateSetColors.Outline,
                        errorBorderColor = CreateSetColors.Error,
                        focusedContainerColor = CreateSetColors.Surface,
                        unfocusedContainerColor = CreateSetColors.SurfaceVariant.copy(alpha = 0.5f)
                    ),
                    singleLine = true
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row {
                    Text(
                        text = "Description",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CreateSetColors.OnSurface
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "(Option)",
                        fontSize = 11.sp,
                        color = CreateSetColors.OnSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
                OutlinedTextField(
                    value = description,
                    onValueChange = onDescChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Describe what this set is about...",
                            color = CreateSetColors.OnSurfaceVariant.copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CreateSetColors.Primary,
                        unfocusedBorderColor = CreateSetColors.Outline,
                        focusedContainerColor = CreateSetColors.Surface,
                        unfocusedContainerColor = CreateSetColors.SurfaceVariant.copy(alpha = 0.5f)
                    ),
                    minLines = 3,
                    maxLines = 5
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Category",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CreateSetColors.OnSurface
                )
                androidx.compose.foundation.layout.FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CATEGORIES.forEach { cat ->
                        CategoryChip(
                            label = cat,
                            isSelected = cat == selectedCategory,
                            onClick = { onCategorySelect(cat) }
                        )
                    }
                }
            }

            HorizontalDivider(color = CreateSetColors.OutlineVariant)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Public Set",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CreateSetColors.OnSurface
                    )
                    Text(
                        text = "Allow others to find and study this set.",
                        fontSize = 12.sp,
                        color = CreateSetColors.OnSurfaceVariant
                    )
                }
                Switch(
                    checked = isPublic,
                    onCheckedChange = onPublicToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = CreateSetColors.Primary,
                        uncheckedTrackColor = CreateSetColors.Outline
                    )
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(
                if (isSelected) CreateSetColors.PrimaryContainer else CreateSetColors.Surface
            )
            .border(
                width = 1.dp,
                color = if (isSelected) CreateSetColors.Primary else CreateSetColors.OutlineVariant,
                shape = CircleShape
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) CreateSetColors.Primary else CreateSetColors.OnSurface
        )
    }
}

@Composable
private fun EmptyWordState(onAddWord: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = CreateSetColors.Surface,
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = CreateSetColors.OutlineVariant,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(CreateSetColors.PrimaryContainer.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LibraryBooks,
                    contentDescription = null,
                    tint = CreateSetColors.Primary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "No words added yet",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CreateSetColors.OnSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Start building your set by adding your first flashcard.",
                    fontSize = 13.sp,
                    color = CreateSetColors.OnSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.widthIn(max = 200.dp)
                )
            }

            OutlinedButton(
                onClick = onAddWord,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    CreateSetColors.OutlineVariant
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = CreateSetColors.Primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Add Word",
                    color = CreateSetColors.Primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun WordEntryCard(
    index: Int,
    word: WordEntry,
    onTermChange: (String) -> Unit,
    onDefinitionChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = CreateSetColors.Surface,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${index + 1}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CreateSetColors.OnSurfaceVariant
                )
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Xoá từ",
                        tint = CreateSetColors.OnSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            HorizontalDivider(color = CreateSetColors.OutlineVariant)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = word.term,
                onValueChange = onTermChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Term", fontSize = 12.sp) },
                placeholder = { Text("Nhập từ...", fontSize = 13.sp) },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CreateSetColors.Primary,
                    unfocusedBorderColor = CreateSetColors.Outline
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = word.definition,
                onValueChange = onDefinitionChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Definition", fontSize = 12.sp) },
                placeholder = { Text("Nhập nghĩa...", fontSize = 13.sp) },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CreateSetColors.Primary,
                    unfocusedBorderColor = CreateSetColors.Outline
                ),
                minLines = 2,
                maxLines = 3
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateNewSetScreenPreview() {
    MinLishMobileTheme {
        CreateNewSetScreen()
    }
}
