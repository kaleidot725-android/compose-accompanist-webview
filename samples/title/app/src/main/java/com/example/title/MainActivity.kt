package com.example.title

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.google.accompanist.web.AccompanistWebChromeClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

class MainActivity : ComponentActivity() {
    object Urls {
        val home = "https://m.yahoo.co.jp/"
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var webPageTitle by remember { mutableStateOf("") }
            val webViewState = rememberWebViewState(url = Urls.home)
            val chromeClient = object : AccompanistWebChromeClient() {
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    webPageTitle = title ?: ""
                    super.onReceivedTitle(view, title)
                }
            }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = webPageTitle,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                },
                content = {
                    WebView(
                        state = webViewState,
                        chromeClient = chromeClient,
                        modifier = Modifier.padding(it)
                    )
                }
            )
        }
    }
}