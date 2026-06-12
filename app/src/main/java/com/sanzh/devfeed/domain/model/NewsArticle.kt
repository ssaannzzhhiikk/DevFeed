package com.sanzh.devfeed.domain.model

data class NewsArticle(
    val id: Long,
    val title: String,
    val url: String?,
    val score: Int,
    val commentCount: Int,
    val author: String,
    val timeAgo: String

)
