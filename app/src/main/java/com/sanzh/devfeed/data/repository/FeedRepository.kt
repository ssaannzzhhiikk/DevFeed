package com.sanzh.devfeed.data.repository

import android.content.Context
import com.sanzh.devfeed.data.local.db.AppDatabase
import com.sanzh.devfeed.data.local.db.BookmarkEntity
import com.sanzh.devfeed.data.remote.RetrofitClient
import com.sanzh.devfeed.domain.model.GithubRepo
import com.sanzh.devfeed.domain.model.NewsArticle
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

class FeedRepository(private val context: Context) {
    private val githubApi = RetrofitClient.githubApi
    private val hnApi = RetrofitClient.hnApi
    private val dao = AppDatabase.getInstance(context).bookmarkDao()
    // Fetch trending repos by language from GitHub API
    suspend fun fetchRepos(language: String): List<GithubRepo> {
        val response = githubApi.searchRepositories("language:$language")
        return response.items.map { dto ->
            GithubRepo(
                id = dto.id,
                name = dto.name,
                fullName = dto.fullName,
                description = dto.description,
                language = dto.language,
                stars = dto.stars,
                forks = dto.forks,
                ownerAvatarUrl = dto.owner.avatarUrl,
                htmlUrl = dto.htmlUrl
            )
        }
    }
    // Fetch top 15 HN stories (IDs first, then each story in parallel)
    suspend fun fetchTopNews(): List<NewsArticle> = coroutineScope {
        val ids = hnApi.getTopStoryIds().take(15)
        ids.map { id -> async { hnApi.getStory(id) } }
            .awaitAll()
            .filter { it.title != null }
            .map { dto ->
                NewsArticle(
                    id = dto.id,
                    title = dto.title!!,
                    url = dto.url,
                    score = dto.score,
                    commentCount = dto.descendants,
                    author = dto.by,
                    timeAgo = formatTimeAgo(dto.time)
                )
            }
    }
    fun getBookmarks(): Flow<List<BookmarkEntity>> = dao.getAllBookmarks()
    suspend fun saveBookmark(entity: BookmarkEntity) = dao.insert(entity)
    suspend fun removeBookmark(id: Long) = dao.deleteById(id)
    suspend fun isBookmarked(id: Long): Boolean = dao.isBookmarked(id)

    suspend fun getRepoById(id: Long): GithubRepo? = try {
        val dto = githubApi.getRepository(id)
        GithubRepo(
            id = dto.id,
            name = dto.name,
            fullName = dto.fullName,
            description = dto.description,
            language = dto.language,
            stars = dto.stars,
            forks = dto.forks,
            ownerAvatarUrl = dto.owner.avatarUrl,
            htmlUrl = dto.htmlUrl
        )
    } catch (_: Exception) { null }

    suspend fun getStoryById(id: Long): NewsArticle? {
        return try {
            val dto = hnApi.getStory(id)
            NewsArticle(
                id = dto.id,
                title = dto.title ?: return null,
                url = dto.url,
                score = dto.score,
                commentCount = dto.descendants,
                author = dto.by,
                timeAgo = formatTimeAgo(dto.time)
            )
        } catch (_: Exception) { null }
    }

    private fun formatTimeAgo(epochSeconds: Long): String {
        val diff = System.currentTimeMillis() / 1000 - epochSeconds
        return when {
            diff < 3600 -> "${diff / 60}m ago"
            diff < 86400 -> "${diff / 3600}h ago"
            else -> "${diff / 86400}d ago"
        }
    }
}
