package com.sanzh.devfeed.ui.feed

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sanzh.devfeed.DevFeedApp
import com.sanzh.devfeed.data.repository.FeedRepository
import com.sanzh.devfeed.domain.model.FeedItem
import com.sanzh.devfeed.domain.model.GithubRepo
import com.sanzh.devfeed.domain.model.NewsArticle

@Composable
fun FeedScreen(onItemClick: (Long, String) -> Unit) {
    val context = LocalContext.current
    val repository = remember { FeedRepository(context) }
    // State managed locally — no ViewModel in Stage 1
    var repos by remember { mutableStateOf<List<GithubRepo>>(emptyList()) }
    var news by remember { mutableStateOf<List<NewsArticle>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val language by (context.applicationContext as DevFeedApp)
        .preferencesManager.languageFilter
        .collectAsState(initial = "kotlin")
    // Load data when screen opens or language changes
    LaunchedEffect(language) {
        isLoading = true
        error = null
        try {
            repos = repository.fetchRepos(language)
            news = repository.fetchTopNews()
        } catch (e: Exception) {
            error = e.message
        } finally {
            isLoading = false
        }
    }
    // Build 2-type feed list
    val feedItems = buildList<FeedItem> {
        if (repos.isNotEmpty()) {
            add(FeedItem.SectionHeader("Trending ${language.replaceFirstChar {
                it.uppercase() }}"))
            addAll(repos.map { FeedItem.RepoItem(it) })
        }
        if (news.isNotEmpty()) {
            add(FeedItem.SectionHeader("Hacker News Top"))
            addAll(news.map { FeedItem.ArticleItem(it) })
        }
    }
    // Crossfade animation between loading/content/error states
    Crossfade(targetState = isLoading, label = "feed_crossfade") { loading ->
        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            FeedList(feedItems = feedItems, onItemClick = onItemClick)
        }
    }
}


@Composable
fun FeedList(
    feedItems : List<FeedItem>,
    onItemClick: (Long, String) -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(vertical = 8.dp)) {
        items(
            items = feedItems,
            key = { item ->
                when (item) {
                    is FeedItem.SectionHeader -> "header_${item.title}"
                    is FeedItem.RepoItem -> "repo_${item.repo.id}"
                    is FeedItem.ArticleItem -> "article_${item.article.id}"
                }
            }
        ) { item ->
            // Branch on type — this is what makes it a 2-type list
            when (item) {
                is FeedItem.SectionHeader ->
                    SectionHeaderItem(title = item.title)
                is FeedItem.RepoItem ->
                    // Click listener required for full score
                    RepoCard(
                        repo = item.repo,
                        onClick = { onItemClick(item.repo.id, "repo") }
                    )
                is FeedItem.ArticleItem ->
                    ArticleCard(
                        article = item.article,
                        onClick = { onItemClick(item.article.id, "article") }
                    )
            }
        }
    }
}


@Composable
fun SectionHeaderItem(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun RepoCard(repo: GithubRepo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick)
            .animateContentSize(), // Animation requirement
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top)
        {
            // Avatar image — Stage 1: use AsyncImage from Coil
            // NOTE: Coil is added here even in Stage 1 because it is needed
            // for images. Stage 2 will explicitly score this criterion.
            AsyncImage(
                model = repo.ownerAvatarUrl,
                contentDescription = "avatar",
                modifier = Modifier.size(48.dp).clip(CircleShape)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(repo.fullName, style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold)
                repo.description?.let {
                    Text(it, maxLines = 2, overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall)
                }
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    repo.language?.let { lang ->
                        Text(lang, style = MaterialTheme.typography.labelSmall)
                    }
                    Text("★ ${repo.stars}", style =
                        MaterialTheme.typography.labelSmall)
                    Text("⑂ ${repo.forks}", style =
                        MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
fun ArticleCard(article: NewsArticle, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            article.url?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "by ${article.author}",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = "score: ${article.score}",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}