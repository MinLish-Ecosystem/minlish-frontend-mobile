package com.minlish.app.presentation.screens.library

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minlish.app.presentation.screens.library.WordListViewModel

data class WordItem(
    val word: String,
    val phonetic: String,
    val status: WordStatus,
    val definition: String,
    val audioUrl: String = ""
)

enum class WordStatus(
    val label: String,
    val color: Color,
    val containerColor: Color,
    val onContainerColor: Color
) {
    MASTERED(
        label = "Mastered",
        color = Color(0xFF10B981), // success
        containerColor = Color(0xFFD1FAE5),
        onContainerColor = Color(0xFF065F46)
    ),
    LEARNING(
        label = "Learning",
        color = Color(0xFFF59E0B), // warning
        containerColor = Color(0xFFFEF3C7),
        onContainerColor = Color(0xFF92400E)
    ),
    NEW(
        label = "New",
        color = Color(0xFF3B82F6), // new
        containerColor = Color(0xFFDBEAFE),
        onContainerColor = Color(0xFF1E3A8A)
    )
}

@Composable
fun WordListScreen(
    setId: String,
    setName: String,
    onBack: () -> Unit,
    onStartSession: (String) -> Unit = {},
    onAddWordClick: (String) -> Unit = {},
    viewModel: WordListViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var showFilterMenu by remember { mutableStateOf(false) }

    val surfaceColor = Color(0xFFFFFFFF)
    val surfaceVariantColor = Color(0xFFF3F4F6)
    val onSurfaceColor = Color(0xFF111827)
    val onSurfaceVariantColor = Color(0xFF4B5563)
    val primaryColor = Color(0xFF4F46E5)
    val primaryContainerColor = Color(0xFFE0E7FF)
    val onPrimaryContainerColor = Color(0xFF312E81)
    val outlineVariantColor = Color(0xFFE5E7EB)

    val words by viewModel.words
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage
    val totalCount by viewModel.totalCount
    val masteredCount by viewModel.masteredCount
    val learningCount by viewModel.learningCount
    val mediaPlayer = remember { MediaPlayer() }
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }
    LaunchedEffect(setId, searchQuery) {
        viewModel.loadWords(setId, searchQuery)
    }

    val wordItems = remember(words) {
        words.map { response ->
            val status = when (response.status?.lowercase()) {
                "mastered" -> WordStatus.MASTERED
                "learning", "review" -> WordStatus.LEARNING
                else -> WordStatus.NEW
            }
            WordItem(
                word = response.word,
                phonetic = response.pronunciation ?: "",
                status = status,
                definition = response.meaning,
                audioUrl = response.audioUrl ?: "" ,
            )
        }
    }

    val filteredWords = remember(searchQuery, wordItems, selectedFilter) {
        val list = if (searchQuery.isBlank()) {
            wordItems
        } else {
            wordItems.filter {
                it.word.contains(searchQuery, ignoreCase = true) ||
                        it.definition.contains(searchQuery, ignoreCase = true)
            }
        }
        when (selectedFilter) {
            "A -> Z" -> list.sortedBy { it.word.lowercase() }
            "Z -> A" -> list.sortedByDescending { it.word.lowercase() }
            "Mastered" -> list.filter { it.status == WordStatus.MASTERED }
            "Learning" -> list.filter { it.status == WordStatus.LEARNING }
            else -> list
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(surfaceVariantColor)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = surfaceColor.copy(alpha = 0.9f),
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = onSurfaceVariantColor
                    )
                }

                Text(
                    text = setName,
                    color = onSurfaceColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = onSurfaceVariantColor
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Header Info Area
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = surfaceColor,
                    shadowElevation = 1.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Essential vocabulary for professional communication, emails, and meetings.",
                            color = onSurfaceVariantColor,
                            fontSize = 14.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CardStats(
                                count = totalCount.toString(),
                                label = "Total",
                                valueColor = onSurfaceColor,
                                containerColor = surfaceVariantColor,
                                labelColor = onSurfaceVariantColor
                            )

                            CardStats(
                                count = masteredCount.toString(),
                                label = "Mastered",
                                valueColor = WordStatus.MASTERED.color,
                                containerColor = WordStatus.MASTERED.containerColor,
                                labelColor = WordStatus.MASTERED.onContainerColor
                            )

                            CardStats(
                                count = learningCount.toString(),
                                label = "Learning",
                                valueColor = WordStatus.LEARNING.color,
                                containerColor = WordStatus.LEARNING.containerColor,
                                labelColor = WordStatus.LEARNING.onContainerColor
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { onStartSession(setId) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryColor,
                                    contentColor = Color.White
                                ),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                             ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null
                                    )
                                    Text(
                                        text = "Start Session",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp
                                    )
                                }
                            }

                            Button(
                                onClick = { onAddWordClick(setId) },
                                modifier = Modifier
                                    .height(48.dp)
                                    .border(
                                        width = 1.dp,
                                        color = primaryColor.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryContainerColor,
                                    contentColor = onPrimaryContainerColor
                                )
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null
                                    )
                                    Text(
                                        text = "Add Word",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Search words in this set...",
                                color = onSurfaceVariantColor.copy(alpha = 0.6f),
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = onSurfaceVariantColor
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = surfaceColor,
                            unfocusedContainerColor = surfaceColor,
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = outlineVariantColor
                        ),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Word List",
                            color = onSurfaceColor,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )

                        Box {
                            TextButton(
                                onClick = { showFilterMenu = true },
                                contentPadding = PaddingValues(horizontal = 8.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Sort,
                                        contentDescription = null,
                                        tint = primaryColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = if (selectedFilter == "All") "Filter" else selectedFilter,
                                        color = primaryColor,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                            DropdownMenu(
                                expanded = showFilterMenu,
                                onDismissRequest = { showFilterMenu = false }
                            ) {
                                listOf("All", "A -> Z", "Z -> A", "Mastered", "Learning").forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            selectedFilter = option
                                            showFilterMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = primaryColor)
                    }
                }
            } else if (errorMessage != null) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = errorMessage!!, color = Color.Red)
                    }
                }
            } else {
                items(filteredWords) { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = surfaceColor),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = item.word,
                                                color = onSurfaceColor,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp
                                            )

                                            IconButton(
                                                onClick = {
                                                    if (item.audioUrl.isNotEmpty()) {
                                                        mediaPlayer.reset()
                                                        mediaPlayer.setDataSource(item.audioUrl)
                                                        mediaPlayer.setOnPreparedListener { mp ->
                                                            mp.start()
                                                        }
                                                        mediaPlayer.setOnErrorListener { mp, what, extra ->
                                                            true
                                                        }
                                                        mediaPlayer.prepareAsync()
                                                    }
                                                },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                                    contentDescription = "Play pronunciation",
                                                    tint = onSurfaceVariantColor,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }

                                        Text(
                                            text = item.phonetic,
                                            color = onSurfaceVariantColor,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 14.sp
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = item.status.containerColor,
                                                shape = RoundedCornerShape(6.dp)
                                            )
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = item.status.label.uppercase(),
                                            color = item.status.onContainerColor,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                    }
                                }

                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    color = outlineVariantColor.copy(alpha = 0.5f),
                                    thickness = 1.dp
                                )

                                Text(
                                    text = item.definition,
                                    color = onSurfaceColor,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .width(4.dp)
                                .height(84.dp)
                                .background(
                                    color = item.status.color,
                                    shape = RoundedCornerShape(
                                        topStart = 12.dp,
                                        bottomStart = 12.dp
                                    )
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.CardStats(
    count: String,
    label: String,
    valueColor: Color,
    containerColor: Color,
    labelColor: Color
) {
    Card(
        modifier = Modifier.weight(1f),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = count,
                color = valueColor,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

            Text(
                text = label.uppercase(),
                color = labelColor,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
