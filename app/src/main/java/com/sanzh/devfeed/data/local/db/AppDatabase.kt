package com.sanzh.devfeed.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BookmarkEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        // manual singleton, then we will use HILT
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java,
                    "devfeed.db")
                    .build().also { INSTANCE = it }
            }
    }
}
