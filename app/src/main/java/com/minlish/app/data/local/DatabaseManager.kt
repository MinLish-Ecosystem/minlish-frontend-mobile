package com.minlish.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.minlish.app.data.local.dao.UserDao
import com.minlish.app.data.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class DatabaseManager: RoomDatabase() {
    abstract fun userDao(): UserDao
}