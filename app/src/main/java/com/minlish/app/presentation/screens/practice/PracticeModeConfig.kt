package com.minlish.app.presentation.screens.practice

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

val ALL_PRACTICE_MODES = listOf(
    PracticeMode(
        id = "smart_review",
        title = "Smart Review (SRS)",
        description = "Optimize your memory with spaced repetition.",
        isComingSoon = false,  // Default value, backend sẽ override
        iconName = Icons.Default.Psychology,
        gradientColor = Brush.horizontalGradient(
            colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
        ),
        buttonText = "Start Review"
    ),
    PracticeMode(
        id = "vocabulary_quiz",
        title = "Vocabulary Quiz",
        description = "Test your knowledge on recent words.",
        isComingSoon = false,
        iconName = Icons.Default.Quiz,
        gradientColor = Brush.horizontalGradient(
            colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
        ),
        buttonText = "Start Quiz"
    ),
    PracticeMode(
        id = "listening_challenge",
        title = "Listening Challenge",
        description = "Improve comprehension with native audio.",
        isComingSoon = true,
        iconName = Icons.Default.Headphones,
        gradientColor = Brush.horizontalGradient(
            colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
        ),
        buttonText = "Coming Soon"
    ),
    PracticeMode(
        id = "writing_practice",
        title = "Writing Practice",
        description = "Construct sentences and get feedback.",
        isComingSoon = true,
        iconName = Icons.Default.Edit,
        gradientColor = Brush.horizontalGradient(
            colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
        ),
        buttonText = "Coming Soon"
    )
)