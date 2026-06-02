package com.minlish.app.di
import android.content.Context
import androidx.room.Room
import com.minlish.app.data.local.DatabaseManager
import com.minlish.app.data.local.dao.UserDao

object DatabaseModule {
    private var database: DatabaseManager? = null

    fun getDatabase(context: Context): DatabaseManager {
        return database ?: Room.databaseBuilder(
            context.applicationContext,
            DatabaseManager::class.java,
            "minlish_database"
        ).build().also { database = it }
    }

    fun getUserDao(context: Context): UserDao {
        return getDatabase(context).userDao()
    }
}