package com.minlish.app.presentation.screens.library

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

data class WordItem(
    val word: String,
    val phonetic: String,
    val status: WordStatus,
    val definition: String
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
    setName: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    // Color theme values
    val surfaceColor = Color(0xFFFFFFFF)
    val surfaceVariantColor = Color(0xFFF3F4F6)
    val onSurfaceColor = Color(0xFF111827)
    val onSurfaceVariantColor = Color(0xFF4B5563)
    val primaryColor = Color(0xFF4F46E5)
    val primaryContainerColor = Color(0xFFE0E7FF)
    val onPrimaryContainerColor = Color(0xFF312E81)
    val outlineVariantColor = Color(0xFFE5E7EB)

    val sampleWords = remember {
        listOf(
            WordItem(
                word = "Leverage",
                phonetic = "/ˈlev.ɚ.ɪdʒ/",
                status = WordStatus.MASTERED,
                definition = "To use something that you already have in order to achieve something new or better."
            ),
            WordItem(
                word = "Synergy",
                phonetic = "/ˈsɪn.ɚ.dʒi/",
                status = WordStatus.LEARNING,
                definition = "The combined power of a group of things when they are working together that is greater than the total power achieved by each working separately."
            ),
            WordItem(
                word = "Paradigm",
                phonetic = "/ˈper.ə.daɪm/",
                status = WordStatus.NEW,
                definition = "A model of something, or a very clear and typical example of something."
            ),
            WordItem(
                word = "Stakeholder",
                phonetic = "/ˈsteɪkˌhoʊl.dɚ/",
                status = WordStatus.MASTERED,
                definition = "A person or group of people who own a share in a business; someone who has an interest in the success of a plan, system, or organization."
            )
        )
    }

    val filteredWords = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            sampleWords
        } else {
            sampleWords.filter {
                it.word.contains(searchQuery, ignoreCase = true) ||
                        it.definition.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(surfaceVariantColor)
    ) {
        // Sticky Header / Navbar
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

                IconButton(onClick = { /* More actions */ }) {
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

                        // Statistics
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Total Card
                            CardStats(
                                count = "42",
                                label = "Total",
                                valueColor = onSurfaceColor,
                                containerColor = surfaceVariantColor,
                                labelColor = onSurfaceVariantColor
                            )

                            // Mastered Card
                            CardStats(
                                count = "12",
                                label = "Mastered",
                                valueColor = WordStatus.MASTERED.color,
                                containerColor = WordStatus.MASTERED.containerColor,
                                labelColor = WordStatus.MASTERED.onContainerColor
                            )

                            // Learning Card
                            CardStats(
                                count = "5",
                                label = "Learning",
                                valueColor = WordStatus.LEARNING.color,
                                containerColor = WordStatus.LEARNING.containerColor,
                                labelColor = WordStatus.LEARNING.onContainerColor
                            )
                        }

                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { /* Start Session */ },
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
                                onClick = { /* Add Word */ },
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

            // Word List Section
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Search Bar
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

                    // Sub-header
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

                        TextButton(
                            onClick = { /* Filter click */ },
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
                                    text = "Filter",
                                    color = primaryColor,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // Word Cards List
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
                                            onClick = { /* Play pronunciation */ },
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

                                // Status Badge
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

                    // Simulated Left Vertical Accent Bar
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .width(4.dp)
                            .height(84.dp) // Covers most of the card height
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
