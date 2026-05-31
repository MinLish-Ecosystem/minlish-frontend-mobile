package com.minlish.app.presentation.screens.vocab

// ─────────────────────────────────────────────────────────────────────────────
// Data model cho Vocabulary Set (dùng nội bộ trong UI layer)
// ─────────────────────────────────────────────────────────────────────────────

enum class AccentColor { CYAN, AMBER, ROSE, PURPLE, EMERALD }

data class VocabSetUiModel(
    val id: String,
    val title: String,
    val description: String,
    val wordCount: Int,
    val masteredPercent: Int,         // 0-100, nếu -1 = chưa bắt đầu
    val accentColor: AccentColor
)
