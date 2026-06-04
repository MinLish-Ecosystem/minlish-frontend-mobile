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



data class MockSetData(
    val title: String,
    val description: String,
    val wordCount: Int,
    val masteryPercent: Int,
    val accentColor: Color,
    val statusColor: Color,
    val statusIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val statusText: String,
    val progressBrush: Brush
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier,
    onSetClick: (String) -> Unit = {},
    onCreateNewSet: () -> Unit = {},
) {
    var selectedSubTab by remember { mutableStateOf("My Sets") }
    var searchQuery by remember { mutableStateOf("") }
    
    // Mock data for "My Sets"
    val mySets = remember {
        listOf(
            MockSetData(
                title = "IELTS Core",
                description = "Essential vocabulary for academic reading and writing.",
                wordCount = 150,
                masteryPercent = 65,
                accentColor = Color(0xFF06B6D4), // accent-cyan
                statusColor = Color(0xFF10B981), // success
                statusIcon = Icons.Default.CheckCircle,
                statusText = "65% Mastered",
                progressBrush = Brush.linearGradient(
                    colors = listOf(Color(0xFFF093FB), Color(0xFFF5576C))
                )
            ),
            MockSetData(
                title = "Phrasal Verbs II",
                description = "Common phrasal verbs for everyday conversation.",
                wordCount = 85,
                masteryPercent = 30,
                accentColor = Color(0xFFF59E0B), // accent-amber
                statusColor = Color(0xFFF59E0B), // warning
                statusIcon = Icons.Default.TrendingUp,
                statusText = "30% Mastered",
                progressBrush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFFF59E0B), Color(0xFFF59E0B))
                )
            ),
            MockSetData(
                title = "Business Idioms",
                description = "Expressions used in professional environments.",
                wordCount = 42,
                masteryPercent = 0,
                accentColor = Color(0xFFF43F5E), // accent-rose
                statusColor = Color(0xFFC7C4D7), // outline-variant
                statusIcon = Icons.Default.HourglassEmpty,
                statusText = "Not started",
                progressBrush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFFE4E1ED), Color(0xFFE4E1ED))
                )
            )
        )
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
                    .background(surfaceContainerHighColor, shape = RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("My Sets", "Explore").forEach { tab ->
                    val isSelected = selectedSubTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) surfaceContainerLowestColor else Color.Transparent)
                            .clickable { selectedSubTab = tab }
                            .padding(vertical = 10.dp)
                            .then(
                                if (isSelected) Modifier.shadow(1.dp, RoundedCornerShape(8.dp)) else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            color = if (isSelected) onSurfaceColor else onSurfaceVariantColor,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
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
                            text = "Search vocabulary sets...",
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

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Featured Set",
                        color = onSurfaceColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(192.dp)
                            .shadow(8.dp, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onSetClick("Business English") }
                    ) {
                        AsyncImage(
                            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuDGpBVgr4REf_p5hjaYzH75aDJ8xz8JzwRTn-niaXN3GUkA_uFtO5ya57A5pskbx7jVWHHCWjDxaiGLhZ2QFk31GuB_M-A6JE-2aMu-DAmOtNMvfJIguazIz7a3D8CW6sQ1H0HoP7L1AVRmGee7t6E3Ncg4s8h_FjVAxYrO31nw4twNypuwvkGtjUMJ3BAdLqgwjMTeho0KqZ5Ux3RsF3MS34dhqNnXZP2E-Fbyre8cJSsrMwU9wkpy_xUWcuI979TCd7Gq3uyA8kw",
                            contentDescription = "Featured Set Background",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color(0xFF111827).copy(alpha = 0.85f)
                                        ),
                                        startY = 100f
                                    )
                                )
                        )

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        primaryColor.copy(alpha = 0.9f),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Business English",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }

                            Text(
                                text = "Tech Startup Meetings",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )

                            Text(
                                text = "Master essential vocabulary for modern tech environments.",
                                color = Color(0xFFE4E1ED),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Trending Topics",
                        color = onSurfaceColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TrendingTopicItem(
                            title = "TOEIC Essential 500",
                            subtitle = "500 terms • Advanced",
                            icon = Icons.Default.Work,
                            accentColor = accentBlueColor,
                            iconBgColor = primary50Color,
                            onClick = { onSetClick("TOEIC Essential 500") }
                        )

                        TrendingTopicItem(
                            title = "Travel & Tourism",
                            subtitle = "120 terms • Intermediate",
                            icon = Icons.Default.FlightTakeoff,
                            accentColor = accentEmeraldColor,
                            iconBgColor = Color(0xFFF5F2FE),
                            onClick = { onSetClick("Travel & Tourism") }
                        )

                        TrendingTopicItem(
                            title = "Dining Out",
                            subtitle = "85 terms • Beginner",
                            icon = Icons.Default.RestaurantMenu,
                            accentColor = accentAmberColor,
                            iconBgColor = Color(0xFFF5F2FE),
                            onClick = { onSetClick("Dining Out") }
                        )
                    }
                }
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Categories",
                        color = onSurfaceColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Everyday Life", "Academic", "Medical", "Legal").forEach { category ->
                            Box(
                                modifier = Modifier
                                    .border(
                                        width = 2.dp,
                                        color = Color(0xFFF0F4FF),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .background(surfaceContainerLowestColor, shape = RoundedCornerShape(20.dp))
                                    .clickable {  }
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = category,
                                    color = onSurfaceColor,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
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
                                    onClick = { onSetClick(setItem.title) }
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
    onClick: () -> Unit
) {
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
                    IconButton(
                        onClick = {},
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = Color(0xFFC7C4D7)
                        )
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
