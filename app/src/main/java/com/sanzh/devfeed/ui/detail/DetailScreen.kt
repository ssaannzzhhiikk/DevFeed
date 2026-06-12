package com.sanzh.devfeed.ui.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sanzh.devfeed.data.repository.FeedRepository
import com.sanzh.devfeed.domain.model.GithubRepo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(itemId: Long, itemType: String, onBack: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { FeedRepository(context) }
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
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (itemType == "repo") {
                RepoDetailContent(itemId = itemId, context = context)
            } else {
                ArticleDetailContent(itemId = itemId, context = context)
            }
        }
    }
}

@Composable
fun RepoDetailContent(itemId: Long, context: Context) {
    // Show repo info + "Open on GitHub" button
    var repo by remember { mutableStateOf<GithubRepo?>(null) }
    LaunchedEffect(itemId) {
        // Find from last fetch — or re-fetch by id
        // Simple approach: open GitHub URL in browser
    }
    Text("Repo ID: $itemId", style = MaterialTheme.typography.bodyLarge)
    Spacer(Modifier.height(12.dp))
    Button(onClick = {
        val intent = Intent(Intent.ACTION_VIEW,
            Uri.parse("https://github.com/"))
        context.startActivity(intent)
    }) {
        Text("Open on GitHub")
    }
}

@Composable
fun ArticleDetailContent(itemId: Long, context: Context) {
    Text("Article ID: $itemId", style = MaterialTheme.typography.bodyLarge)
    // Add more article details here later
}
