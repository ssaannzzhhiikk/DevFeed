package com.sanzh.devfeed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sanzh.devfeed.ui.DevFeedApp
import com.sanzh.devfeed.ui.theme.DevFeedTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DevFeedTheme {
                DevFeedApp()
            }
        }
    }
}