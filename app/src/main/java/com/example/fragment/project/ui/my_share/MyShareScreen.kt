package com.example.fragment.project.ui.my_share

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fragment.project.WanTheme
import com.example.fragment.project.components.ArticleCard
import com.example.fragment.project.components.SwipeRefreshBox
import com.example.fragment.project.components.TitleBar

@Composable
fun MyShareScreen(
    viewModel: MyShareViewModel = viewModel(),
    onNavigateToLogin: () -> Unit = {},
    onNavigateToSystem: (cid: String) -> Unit = {},
    onNavigateToUser: (userId: String) -> Unit = {},
    onNavigateToWeb: (url: String) -> Unit = {},
    onNavigateUp: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TitleBar(
                title = "我分享的文章",
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                })
        }
    ) { innerPadding ->
        SwipeRefreshBox(
            items = uiState.result,
            isRefreshing = uiState.isRefreshing,
            isLoading = uiState.isLoading,
            isFinishing = uiState.isFinishing,
            onRefresh = { viewModel.getHome() },
            onLoad = { viewModel.getNext() },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            key = { _, item -> item.id },
        ) { _, item ->
            ArticleCard(
                data = item,
                onNavigateToLogin = onNavigateToLogin,
                onNavigateToUser = onNavigateToUser,
                onNavigateToSystem = onNavigateToSystem,
                onNavigateToWeb = onNavigateToWeb
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
fun MyShareScreenPreview() {
    WanTheme { MyShareScreen() }
}