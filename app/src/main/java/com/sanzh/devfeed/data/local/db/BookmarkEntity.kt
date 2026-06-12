package com.sanzh.devfeed.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity (
    @PrimaryKey val id : Long,
    val title : String,
    val url : String,
    val type : String, // "repo" or "article"
    val subtitle : String?,
    val imageUrl : String?,
    val savedAt : Long = System.currentTimeMillis()
)