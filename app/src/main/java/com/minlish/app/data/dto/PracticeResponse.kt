package com.minlish.app.data.dto

data class PracticeModeDto(
    val id: String,
    val practiceModes: List<ModeDto>
)
data class ModeDto(
    val id: String,
    var isComingSoon: Boolean,
)