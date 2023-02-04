package com.example.routing

import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

class MainActivity : ComponentActivity() {
    object Urls {
        val home = "https://m.yahoo.co.jp/"
        val mail = "https://mail.yahoo.co.jp/"
    }

    sealed class ScreenState {
        object Home : ScreenState()
        object Mail : ScreenState()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var screenState by remember {
                mutableStateOf<ScreenState>(ScreenState.Home)
            }

            when (screenState) {
                ScreenState.Home -> {
                    HomeScreen(
                        shouldOverrideUrlLoading = { url ->
                            if (url == Urls.mail) {
                                screenState = ScreenState.Mail
                                return@HomeScreen true
                            }
                            return@HomeScreen false
                        }
                    )
                }

                is ScreenState.Mail -> {
                    MailScreen(
                        onBack = {
                            screenState = ScreenState.Home
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(shouldOverrideUrlLoading: (String) -> Boolean) {
    val webViewState = rememberWebViewState(url = MainActivity.Urls.home)
    val client = object : AccompanistWebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return shouldOverrideUrlLoading(request?.url.toString())
        }
    }

    WebView(state = webViewState, client = client)
}

@Composable
private fun MailScreen(onBack: () -> Unit) {
    BackHandler(true) {
        onBack()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(100) { count ->
            Card {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = "SAMPLE MAIL $count",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}