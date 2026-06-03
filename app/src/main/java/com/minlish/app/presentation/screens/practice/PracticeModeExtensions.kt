package com.minlish.app.presentation.screens.practice

import com.minlish.app.presentation.navigation.NavDestinations

fun PracticeMode.getRoute(): String? {
    return when (id) {
        "smart_review" -> NavDestinations.FlashCardTest.route  // hoặc route riêng cho Smart Review
        "vocabulary_quiz" -> NavDestinations.FlashCardTest.route  // hoặc route riêng cho Quiz
        "listening_challenge" -> null  // Coming soon
        "writing_practice" -> null  // Coming soon
        else -> null
    }
}