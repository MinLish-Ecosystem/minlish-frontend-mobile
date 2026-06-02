package com.minlish.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val role: String,
    val avatar: String?,
    val isVerified: Boolean,
    val createdAt: String?,
    val updatedAt: String?
)
