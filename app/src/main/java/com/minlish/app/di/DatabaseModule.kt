package com.minlish.app.di
import android.content.Context
import androidx.room.Room
import com.minlish.app.data.local.DatabaseManager
import com.minlish.app.data.local.dao.UserDao

object DatabaseModule {
    private var database: DatabaseManager? = null
}