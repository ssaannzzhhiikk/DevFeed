package com.sanzh.devfeed.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sanzh.devfeed.data.local.db.BookmarkEntity
import com.sanzh.devfeed.data.repository.FeedRepository
import com.sanzh.devfeed.domain.model.GithubRepo
import com.sanzh.devfeed.domain.model.NewsArticle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(itemId: Long, itemType: String, onBack: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { FeedRepository(context) }
    val scope = rememberCoroutineScope()

    var isBookmarked by remember { mutableStateOf(false) }
    var repo by remember { mutableStateOf<GithubRepo?>(null) }
    var article by remember { mutableStateOf<NewsArticle?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(itemId) {
        isBookmarked = repository.isBookmarked(itemId)
        if (itemType == "repo") {
            repo = repository.getRepoById(itemId)
        } else {
            article = repository.getStoryById(itemId)
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (itemType == "repo") "Repository" else "Article")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            if (isBookmarked) {
                                repository.removeBookmark(itemId)
                            } else {
                                val entity = if (itemType == "repo" && repo != null) {
                                    BookmarkEntity(
                                        id = repo!!.id,
                                        title = repo!!.fullName,
                                        url = repo!!.htmlUrl,
                                        type = "repo",
                                        subtitle = "${repo!!.language ?: ""} · ⭐${repo!!.stars}",
                                        imageUrl = repo!!.ownerAvatarUrl
                                    )
                                } else if (article != null) {
                                    BookmarkEntity(
                                        id = article!!.id,
                                        title = article!!.title,
                                        url = article!!.url ?: "",
                                        type = "article",
                                        subtitle = "by ${article!!.author} · score: ${article!!.score}",
                                        imageUrl = null
                                    )
                                } else null
                                entity?.let { repository.saveBookmark(it) }
                            }
                            isBookmarked = !isBookmarked
                        }
                    }) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark
                            else Icons.Outlined.BookmarkBorder,
                            contentDescription = if (isBookmarked) "Remove bookmark"
                            else "Add bookmark",
                            tint = if (isBookmarked) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    if (itemType == "repo") {
                        RepoDetailContent(repo = repo, context = LocalContext.current)
                    } else {
                        ArticleDetailContent(article = article, context = LocalContext.current)
                    }
                }
            }
        }
    }
}

@Composable
fun RepoDetailContent(repo: GithubRepo?, context: android.content.Context) {
    if (repo == null) {
        Text("Failed to load repository.", style = MaterialTheme.typography.bodyLarge)
        return
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = repo.ownerAvatarUrl,
            contentDescription = "avatar",
            modifier = Modifier.size(56.dp).clip(CircleShape)
        )
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(
                text = repo.fullName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            repo.language?.let {
                Text(it, style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary)
            }
        }
    }

    Spacer(Modifier.height(12.dp))

    repo.description?.let {
        Text(it, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(12.dp))
    }

    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("⭐ ${repo.stars}", style = MaterialTheme.typography.bodyMedium)
        Text("🍴 ${repo.forks}", style = MaterialTheme.typography.bodyMedium)
    }

    Spacer(Modifier.height(24.dp))

    Button(
        onClick = {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(repo.htmlUrl))
            )
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Open on GitHub")
    }
}

@Composable
fun ArticleDetailContent(article: NewsArticle?, context: android.content.Context) {
    if (article == null) {
        Text("Failed to load article.", style = MaterialTheme.typography.bodyLarge)
        return
    }

    Text(
        text = article.title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )

    Spacer(Modifier.height(8.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("▲ ${article.score}", style = MaterialTheme.typography.labelMedium)
        Text("💬 ${article.commentCount}", style = MaterialTheme.typography.labelMedium)
        Text("by ${article.author}", style = MaterialTheme.typography.labelMedium)
        Text(article.timeAgo, style = MaterialTheme.typography.labelMedium)
    }

    Spacer(Modifier.height(24.dp))

    if (article.url != null) {
        Button(
            onClick = {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Read Article")
        }
    } else {
        Text(
            "No external link available.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    Spacer(Modifier.height(8.dp))

    OutlinedButton(
        onClick = {
            context.startActivity(
                Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://news.ycombinator.com/item?id=${article.id}"))
            )
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("View Comments on HN")
    }
}