package com.minlish.app.presentation.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minlish.app.presentation.screens.vocab.VocabViewModel



data class MockSetData(
    val id: String,
    val title: String,
    val description: String,
    val wordCount: Int,
    val masteryPercent: Int,
    val accentColor: Color,
    val statusColor: Color,
    val statusIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val statusText: String,
    val progressBrush: Brush,
    val category: String,
    val isPublic: Boolean
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier,
    onSetClick: (String, String, Boolean) -> Unit = { _, _, _ -> },
    onCreateNewSet: () -> Unit = {},
    viewModel: VocabViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedSubTab by remember { mutableStateOf("My Sets") }
    var searchQuery by remember { mutableStateOf("") }
    
    LaunchedEffect(selectedSubTab, searchQuery) {
        if (selectedSubTab == "My Sets") {
            viewModel.loadSets(searchQuery)
        } else {
            viewModel.loadPublicSets(searchQuery)
        }
    }
    
    // Map backend sets to "My Sets" UI representation
    val mySets = remember(uiState.sets) {
        uiState.sets.map { setResponse ->
            val colorTheme = setResponse.colorTheme
            val accentColor = when (colorTheme) {
                "blue" -> Color(0xFF3B82F6)
                "emerald" -> Color(0xFF10B981)
                "amber" -> Color(0xFFF59E0B)
                "purple" -> Color(0xFF8B5CF6)
                "rose" -> Color(0xFFF43F5E)
                "cyan" -> Color(0xFF06B6D4)
                else -> Color(0xFF8B5CF6)
            }
            val progressBrush = when (colorTheme) {
                "blue" -> Brush.horizontalGradient(listOf(Color(0xFF3B82F6), Color(0xFF60A5FA)))
                "emerald" -> Brush.horizontalGradient(listOf(Color(0xFF10B981), Color(0xFF34D399)))
                "amber" -> Brush.horizontalGradient(listOf(Color(0xFFF59E0B), Color(0xFFFBBF24)))
                "purple" -> Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFFA78BFA)))
                "rose" -> Brush.horizontalGradient(listOf(Color(0xFFF43F5E), Color(0xFFFB7185)))
                "cyan" -> Brush.horizontalGradient(listOf(Color(0xFF06B6D4), Color(0xFF22D3EE)))
                else -> Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFFA78BFA)))
            }
            val masteryPct = setResponse.progress?.masteredPct ?: 0
            val statusColor = if (masteryPct == 100) Color(0xFF10B981) else if (masteryPct > 0) Color(0xFFF59E0B) else Color(0xFFC7C4D7)
            val statusIcon = if (masteryPct == 100) Icons.Default.CheckCircle else if (masteryPct > 0) Icons.Default.TrendingUp else Icons.Default.HourglassEmpty
            val statusText = if (masteryPct > 0) "$masteryPct% Mastered" else "Not started"
            
            MockSetData(
                id = setResponse.id,
                title = setResponse.name,
                description = setResponse.description ?: "",
                wordCount = setResponse.totalWords,
                masteryPercent = masteryPct,
                accentColor = accentColor,
                statusColor = statusColor,
                statusIcon = statusIcon,
                statusText = statusText,
                progressBrush = progressBrush,
                category = setResponse.category,
                isPublic = setResponse.isPublic
            )
        }
    }

    val exploreSets = remember(uiState.publicSets) {
        uiState.publicSets.map { setResponse ->
            val colorTheme = setResponse.colorTheme
            val accentColor = when (colorTheme) {
                "blue" -> Color(0xFF3B82F6)
                "emerald" -> Color(0xFF10B981)
                "amber" -> Color(0xFFF59E0B)
                "purple" -> Color(0xFF8B5CF6)
                "rose" -> Color(0xFFF43F5E)
                "cyan" -> Color(0xFF06B6D4)
                else -> Color(0xFF8B5CF6)
            }
            val progressBrush = when (colorTheme) {
                "blue" -> Brush.horizontalGradient(listOf(Color(0xFF3B82F6), Color(0xFF60A5FA)))
                "emerald" -> Brush.horizontalGradient(listOf(Color(0xFF10B981), Color(0xFF34D399)))
                "amber" -> Brush.horizontalGradient(listOf(Color(0xFFF59E0B), Color(0xFFFBBF24)))
                "purple" -> Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFFA78BFA)))
                "rose" -> Brush.horizontalGradient(listOf(Color(0xFFF43F5E), Color(0xFFFB7185)))
                "cyan" -> Brush.horizontalGradient(listOf(Color(0xFF06B6D4), Color(0xFF22D3EE)))
                else -> Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFFA78BFA)))
            }
            MockSetData(
                id = setResponse.id,
                title = setResponse.name,
                description = setResponse.description ?: "",
                wordCount = setResponse.totalWords,
                masteryPercent = 0,
                accentColor = accentColor,
                statusColor = Color(0xFF464554),
                statusIcon = Icons.Default.People,
                statusText = "${setResponse.learnerCount} learners",
                progressBrush = progressBrush,
                category = setResponse.category,
                isPublic = setResponse.isPublic
            )
        }
    }

    // Color definitions based on the HTML mockup
    val surfaceColor = Color(0xFFFCF8FF)
    val onSurfaceColor = Color(0xFF1B1B23)
    val onSurfaceVariantColor = Color(0xFF464554)
    val surfaceContainerHighColor = Color(0xFFE9E6F3)
    val surfaceContainerLowestColor = Color(0xFFFFFFFF)
    val primaryColor = Color(0xFF4648D4)
    val primary50Color = Color(0xFFF0F4FF)
    val outlineVariantColor = Color(0xFFC7C4D7)
    val accentBlueColor = Color(0xFF3B82F6)
    val accentEmeraldColor = Color(0xFF10B981)
    val accentAmberColor = Color(0xFFF59E0B)


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(surfaceColor)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Tab Switcher ("My Sets" and "Explore")
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(surfaceContainerHighColor)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("My Sets", "Explore").forEach { tab ->
                    val isSelected = selectedSubTab == tab
                    if (isSelected) {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedSubTab = tab },
                            shape = RoundedCornerShape(6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = surfaceContainerLowestColor
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = tab,
                                    color = onSurfaceColor,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { selectedSubTab = tab }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab,
                                color = onSurfaceVariantColor,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }


        if (selectedSubTab == "Explore") {
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Search public vocabulary sets...",
                            color = onSurfaceVariantColor,
                            fontSize = 15.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search icon",
                            tint = onSurfaceVariantColor
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = surfaceContainerLowestColor,
                        unfocusedContainerColor = surfaceContainerLowestColor,
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = outlineVariantColor,
                        focusedTextColor = onSurfaceColor,
                        unfocusedTextColor = onSurfaceColor
                    ),
                    singleLine = true
                )
            }

            if (exploreSets.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(color = primaryColor)
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FolderOpen,
                                    contentDescription = "Empty Folder",
                                    tint = onSurfaceVariantColor,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    text = "No public sets found",
                                    color = onSurfaceVariantColor,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            } else {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Public Vocabulary Sets",
                            color = onSurfaceColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            exploreSets.forEach { setItem ->
                                VocabularySetCard(
                                    data = setItem,
                                    onClick = { onSetClick(setItem.id, setItem.title, true) },
                                    onRename = { _, _ -> },
                                    onDelete = {},
                                    isEditable = false
                                )
                            }
                        }
                    }
                }
            }
        } else {
            if (mySets.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(color = primaryColor)
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FolderOpen,
                                    contentDescription = "Empty Folder",
                                    tint = onSurfaceVariantColor,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    text = "Your sets will appear here",
                                    color = onSurfaceVariantColor,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            } else {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "My Vocabulary Sets",
                            color = onSurfaceColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            mySets.forEach { setItem ->
                                VocabularySetCard(
                                    data = setItem,
                                    onClick = { onSetClick(setItem.id, setItem.title, false) },
                                    onRename = { name, desc ->
                                        viewModel.updateSet(setItem.id, name, desc, setItem.category, setItem.isPublic)
                                    },
                                    onDelete = {
                                        viewModel.deleteSet(setItem.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        }

        if (selectedSubTab == "My Sets") {
            FloatingActionButton(
                onClick = onCreateNewSet,
                containerColor = Color.Transparent,
                contentColor = Color.White,
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
                        ),
                        shape = RoundedCornerShape(50)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create New Set",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun VocabularySetCard(
    data: MockSetData,
    onClick: () -> Unit,
    onRename: (newName: String, newDesc: String) -> Unit,
    onDelete: () -> Unit,
    isEditable: Boolean = true
) {
    var showMenu by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    var renameName by remember { mutableStateOf(data.title) }
    var renameDesc by remember { mutableStateOf(data.description) }

    if (showRenameDialog) {
        AlertDialog(
            onDismissRequest = { showRenameDialog = false },
            title = { Text("Rename Vocabulary Set") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = renameName,
                        onValueChange = { renameName = it },
                        label = { Text("Set Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = renameDesc,
                        onValueChange = { renameDesc = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (renameName.isNotBlank()) {
                            onRename(renameName, renameDesc)
                            showRenameDialog = false
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Delete Vocabulary Set") },
            text = { Text("Are you sure you want to delete this vocabulary set? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(data.accentColor)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = data.title,
                            color = Color(0xFF1B1B23),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = data.description,
                            color = Color(0xFF464554),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    if (isEditable) {
                        Box {
                            IconButton(
                                onClick = { showMenu = true },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More options",
                                    tint = Color(0xFFC7C4D7)
                                )
                            }
                            DropdownMenu(
                                expanded = showMenu,
                                	onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Rename") },
                                    onClick = {
                                        showMenu = false
                                        showRenameDialog = true
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Delete") },
                                    onClick = {
                                        showMenu = false
                                        showDeleteConfirmDialog = true
                                    }
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Style,
                            contentDescription = null,
                            tint = Color(0xFF464554),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${data.wordCount} words",
                            color = Color(0xFF464554),
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = data.statusIcon,
                            contentDescription = null,
                            tint = data.statusColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = data.statusText,
                            color = data.statusColor,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFE4E1ED))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(data.masteryPercent / 100f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(50))
                            .background(data.progressBrush)
                    )
                }
            }
        }
    }
}

@Composable
fun TrendingTopicItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    iconBgColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(accentColor)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 12.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(iconBgColor, shape = RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = Color(0xFF1B1B23),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = subtitle,
                    color = Color(0xFF464554),
                    fontSize = 14.sp
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Go",
                tint = Color(0xFFC7C4D7)
            )
        }
    }
}
