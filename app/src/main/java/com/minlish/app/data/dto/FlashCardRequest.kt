package com.minlish.app.data.dto

// data/dto/ReviewItemDto.kt


// data/dto/BatchSubmitReviewDto.kt
data class BatchSubmitReviewDto(
    val reviews: List<ReviewItemDto>
)
data class ReviewItemDto(
    val wordId: String,
    val setId: String,
    val rating: String,
    val reviewedAt: String? = null,
    val timeSpent: Int? = null
)

