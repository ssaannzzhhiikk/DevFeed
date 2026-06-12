package com.sanzh.devfeed.domain.model

// This sealed class is the key to the 2-type list requirement.
// LazyColumn branches on RepoItem vs ArticleItem and renders different UI.
sealed class FeedItem {
    data class RepoItem(val repo: GithubRepo) : FeedItem()
    data class ArticleItem(val article: NewsArticle) : FeedItem()
    data class SectionHeader(val title: String) : FeedItem()
}
