package com.minlish.app.presentation.screens.vocab

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.app.ui.theme.MinLishMobileTheme

// ─────────────────────────────────────────────────────────────────────────────
// Màu sắc theo design system từ Stitch (My Library screen)
// ─────────────────────────────────────────────────────────────────────────────
private object LibraryColors {
    val Background       = Color(0xFFF9FAFB)
    val Surface          = Color(0xFFFFFFFF)
    val SurfaceVariant   = Color(0xFFE9E6F3)
    val OnSurface        = Color(0xFF1B1B23)
    val OnSurfaceVariant = Color(0xFF464554)
    val OutlineVariant   = Color(0xFFC7C4D7)
    val Primary          = Color(0xFF4648D4)
    val GradientStart    = Color(0xFF667EEA)
    val GradientEnd      = Color(0xFF764BA2)
    val AccentCyan       = Color(0xFF06B6D4)
    val AccentAmber      = Color(0xFFF59E0B)
    val AccentRose       = Color(0xFFF43F5E)
    val AccentPurple     = Color(0xFF8127CF)
    val AccentEmerald    = Color(0xFF10B981)
    val Success          = Color(0xFF10B981)
    val Warning          = Color(0xFFF59E0B)
    val ProgressGradientStart = Color(0xFFF093FB)
    val ProgressGradientEnd   = Color(0xFFF5576C)
}

private fun accentColorOf(color: AccentColor): Color = when (color) {
    AccentColor.CYAN    -> LibraryColors.AccentCyan
    AccentColor.AMBER   -> LibraryColors.AccentAmber
    AccentColor.ROSE    -> LibraryColors.AccentRose
    AccentColor.PURPLE  -> LibraryColors.AccentPurple
    AccentColor.EMERALD -> LibraryColors.AccentEmerald
}

// ─────────────────────────────────────────────────────────────────────────────
// MOCK DATA — Dữ liệu mẫu hiển thị khi chưa kết nối API
// TODO: Xoá phần này và bỏ comment phần "Kết nối API" bên dưới khi backend sẵn sàng
// ─────────────────────────────────────────────────────────────────────────────
private val mockSets = mutableListOf(
    VocabSetUiModel(
        id = "1",
        title = "IELTS Core Vocabulary",
        description = "Essential vocabulary for academic reading and writing.",
        wordCount = 150,
        masteredPercent = 65,
        accentColor = AccentColor.CYAN
    ),
    VocabSetUiModel(
        id = "2",
        title = "Phrasal Verbs II",
        description = "Common phrasal verbs for everyday conversation.",
        wordCount = 85,
        masteredPercent = 30,
        accentColor = AccentColor.AMBER
    ),
    VocabSetUiModel(
        id = "3",
        title = "Business Idioms",
        description = "Expressions used in professional environments.",
        wordCount = 42,
        masteredPercent = -1,
        accentColor = AccentColor.ROSE
    )
)

// ─────────────────────────────────────────────────────────────────────────────
// MyLibraryScreen — Màn hình chính
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun MyLibraryScreen(
    onCreateSetClick: () -> Unit = {},
    onSetClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTab  by remember { mutableStateOf(0) }
    var searchQuery  by remember { mutableStateOf("") }

    // ── State dữ liệu (MOCK — chưa kết nối API) ─────────────────────────────
    // Dùng remember để list thay đổi được trong session hiện tại
    val sets = remember { mutableStateListOf<VocabSetUiModel>().also { it.addAll(mockSets) } }

    // ── Filter theo search ───────────────────────────────────────────────────
    val filtered = if (searchQuery.isBlank()) sets.toList()
                   else sets.filter {
                       it.title.contains(searchQuery, ignoreCase = true) ||
                       it.description.contains(searchQuery, ignoreCase = true)
                   }

    Box(modifier = modifier.fillMaxSize().background(LibraryColors.Background)) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Tab Bar ──────────────────────────────────────────────────────
            item {
                LibraryTabBar(selectedTab = selectedTab, onTabSelected = { selectedTab = it })
            }

            // ── Tiêu đề ──────────────────────────────────────────────────────
            item {
                Text("My Library", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = LibraryColors.OnSurface)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Manage your vocabulary sets and progress.", fontSize = 14.sp, color = LibraryColors.OnSurfaceVariant)
            }

            // ── Search Bar ───────────────────────────────────────────────────
            item {
                LibrarySearchBar(query = searchQuery, onQueryChange = { searchQuery = it })
            }

            // ── Danh sách hoặc empty state ───────────────────────────────────
            if (filtered.isEmpty()) {
                item {
                    Box(
                        Modifier.fillMaxWidth().padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.LibraryBooks, null, tint = LibraryColors.OutlineVariant, modifier = Modifier.size(48.dp))
                            Text("Chưa có bộ từ nào", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = LibraryColors.OnSurfaceVariant)
                            Text("Nhấn + để tạo bộ từ đầu tiên", fontSize = 13.sp, color = LibraryColors.OnSurfaceVariant.copy(0.6f))
                        }
                    }
                }
            } else {
                items(filtered, key = { it.id }) { set ->
                    VocabSetCard(set = set, onClick = { onSetClick(set.id) })
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        // ── FAB ──────────────────────────────────────────────────────────────
        FloatingActionButton(
            onClick = onCreateSetClick,
            containerColor = Color.Transparent,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 96.dp)
                .size(56.dp)
                .background(
                    brush = Brush.linearGradient(listOf(LibraryColors.GradientStart, LibraryColors.GradientEnd)),
                    shape = CircleShape
                )
                .shadow(elevation = 8.dp, shape = CircleShape)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Tạo bộ từ mới", modifier = Modifier.size(28.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Hàm này được gọi từ MainActivity khi người dùng tạo set mới
// Thêm set vào danh sách mock (trong session hiện tại)
// TODO: Thay bằng gọi API khi backend sẵn sàng
// ─────────────────────────────────────────────────────────────────────────────
fun addMockSet(title: String, description: String, category: String, isPublic: Boolean) {
    val accent = when (category) {
        "Business"  -> AccentColor.AMBER
        "Academic"  -> AccentColor.CYAN
        "Travel"    -> AccentColor.EMERALD
        "Casual"    -> AccentColor.ROSE
        else        -> AccentColor.PURPLE
    }
    mockSets.add(
        0,  // thêm vào đầu danh sách
        VocabSetUiModel(
            id = System.currentTimeMillis().toString(),
            title = title,
            description = description,
            wordCount = 0,
            masteredPercent = -1,
            accentColor = accent
        )
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Tab Bar
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun LibraryTabBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("My Sets", "Explore")
    Surface(shape = RoundedCornerShape(10.dp), color = LibraryColors.SurfaceVariant, modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(4.dp)) {
            tabs.forEachIndexed { index, label ->
                val isSelected = selectedTab == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) LibraryColors.Surface else Color.Transparent)
                        .clickable { onTabSelected(index) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) LibraryColors.OnSurface else LibraryColors.OnSurfaceVariant
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Search Bar
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun LibrarySearchBar(query: String, onQueryChange: (String) -> Unit) {
    Surface(shape = RoundedCornerShape(12.dp), color = LibraryColors.Surface, shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = LibraryColors.OnSurfaceVariant, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                if (query.isEmpty()) {
                    Text("Search your library...", fontSize = 14.sp, color = LibraryColors.OnSurfaceVariant.copy(alpha = 0.6f))
                }
                androidx.compose.foundation.text.BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp, color = LibraryColors.OnSurface),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// VocabSetCard
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun VocabSetCard(set: VocabSetUiModel, onClick: () -> Unit = {}) {
    val accentColor = accentColorOf(set.accentColor)
    val progressTarget = if (set.masteredPercent >= 0) set.masteredPercent / 100f else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progressTarget,
        animationSpec = tween(durationMillis = 800),
        label = "progress_${set.id}"
    )

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = LibraryColors.Surface,
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(accentColor))
            Column(modifier = Modifier.padding(16.dp)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = set.title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = LibraryColors.OnSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = set.description, fontSize = 13.sp, color = LibraryColors.OnSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Tùy chọn", tint = LibraryColors.OutlineVariant, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    StatChip(icon = Icons.Default.Style, label = "${set.wordCount} words", color = LibraryColors.OnSurfaceVariant)
                    MasteryChip(masteredPercent = set.masteredPercent)
                }
                Spacer(modifier = Modifier.height(10.dp))
                ProgressBar(progress = animatedProgress, accentColor = accentColor, masteredPercent = set.masteredPercent)
            }
        }
    }
}

@Composable
private fun StatChip(icon: ImageVector, label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = color)
    }
}

@Composable
private fun MasteryChip(masteredPercent: Int) {
    when {
        masteredPercent < 0  -> StatChip(Icons.Default.HourglassEmpty, "Not started", LibraryColors.OutlineVariant)
        masteredPercent >= 50 -> StatChip(Icons.Default.CheckCircle, "$masteredPercent% Mastered", LibraryColors.Success)
        else                  -> StatChip(Icons.Default.TrendingUp, "$masteredPercent% Mastered", LibraryColors.Warning)
    }
}

@Composable
private fun ProgressBar(progress: Float, accentColor: Color, masteredPercent: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(CircleShape)
            .background(LibraryColors.OutlineVariant.copy(alpha = 0.4f))
    ) {
        if (masteredPercent >= 50) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(brush = Brush.linearGradient(listOf(LibraryColors.ProgressGradientStart, LibraryColors.ProgressGradientEnd)))
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(accentColor)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Preview
// ─────────────────────────────────────────────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyLibraryScreenPreview() {
    MinLishMobileTheme { MyLibraryScreen() }
}
