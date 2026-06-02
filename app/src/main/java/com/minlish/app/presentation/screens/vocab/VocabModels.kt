package com.minlish.app.presentation.screens.vocab

enum class AccentColor { CYAN, AMBER, ROSE, PURPLE, EMERALD }

data class VocabSetUiModel(
    val id: String,
    val title: String,
    val description: String,
    val wordCount: Int,
    val masteredPercent: Int,
    val accentColor: AccentColor
)
