package com.sanzh.devfeed.domain.model

data class GithubRepo(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val language: String?,
    val stars: Int,
    val forks: Int,
    val ownerAvatarUrl: String,
    val htmlUrl: String
)
