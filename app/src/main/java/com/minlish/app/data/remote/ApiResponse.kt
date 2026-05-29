package com.minlish.app.data.remote

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null,
    val errorCode: String? = null,
    val errors: List<ValidationError>? = null
)

data class ValidationError(
    val field: String,
    val message: String
)
