package com.sanzh.devfeed.ui.bookmarks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sanzh.devfeed.data.local.db.AppDatabase
import com.sanzh.devfeed.data.local.db.BookmarkEntity

@Composable
fun BookmarksScreen(onItemClick: (Long, String) -> Unit) {
    val context = LocalContext.current
    val dao = AppDatabase.getInstance(context).bookmarkDao()
    // Collect Room Flow — updates automatically when DB changes
    val bookmarks by dao.getAllBookmarks().collectAsState(initial = emptyList())
    if (bookmarks.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No bookmarks yet. Tap a repo or article to save.",
                textAlign = TextAlign.Center)
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(vertical = 8.dp)) {
            items(bookmarks, key = { it.id }) { bookmark ->
                BookmarkItem(
                    bookmark = bookmark,
                    onClick = { onItemClick(bookmark.id, bookmark.type) }
                )
            }
        }
    }
}
@Composable
fun BookmarkItem(bookmark: BookmarkEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(bookmark.title, style = MaterialTheme.typography.bodyLarge)
            bookmark.subtitle?.let {
                Text(it, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
