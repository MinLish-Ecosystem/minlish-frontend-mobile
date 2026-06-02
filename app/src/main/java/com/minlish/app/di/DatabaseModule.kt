package com.minlish.app.di
import android.content.Context
import androidx.room.Room
import com.minlish.app.data.local.DatabaseManager
import com.minlish.app.data.local.dao.UserDao

object DatabaseModule {
    private var database: DatabaseManager? = null

    fun init(context: Context) {
        if (database == null) {
            database = Room.databaseBuilder(
                context.applicationContext,
                DatabaseManager::class.java,
                "minlish_database"
            ).fallbackToDestructiveMigration().build()
        }
    }

    val userDao: UserDao
        get() = database?.userDao() ?: throw IllegalStateException("Database not initialized. Please call DatabaseModule.init(context) in Application class.")
}