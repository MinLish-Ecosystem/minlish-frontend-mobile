package com.minlish.app.data.dto.request

data class UpdateProfileRequest(
    val name: String,
    val avatar: String? = null
)
