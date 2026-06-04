package com.minlish.app.data.dto.response

import com.minlish.app.data.dto.UserDto

data class VerifyEmailData(
    val accessToken: String,
    val refreshToken: String,
    val user: UserDto
)