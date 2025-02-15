package karel.hudera.novak.presentation.articles

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import karel.hudera.novak.domain.model.Article
import karel.hudera.novak.presentation.common.ArticlesList
import karel.hudera.novak.presentation.common.ShimmerEffect


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticlesScreen(
    viewModel: ArticlesViewModel = hiltViewModel(),
    navigateToDetail: (Article) -> Unit
) {
    val news = viewModel.news.collectAsLazyPagingItems()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val refreshState = rememberPullToRefreshState()
    val isRefreshing by remember { derivedStateOf { uiState is ArticlesUiState.Loading } }

    val titles by remember {
        derivedStateOf {
            if (news.itemCount > 10) {
                news.itemSnapshotList.items
                    .slice(IntRange(start = 0, endInclusive = 9))
                    .joinToString(separator = " \uD83D\uDFE5 ") { it.title }
            } else {
                ""
            }
        }
    }

    LaunchedEffect(news.loadState) {
        viewModel.handlePagingState(news)
    }

    Scaffold { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            titles.let {
                AnimatedVisibility(true) {
                    Text(
                        text = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .basicMarquee(),
                        fontSize = 12.sp,
                    )
                }
            }

            HorizontalDivider()
            PullToRefreshBox(
                modifier = Modifier
                    .fillMaxSize(),
                state = refreshState,
                isRefreshing = isRefreshing,
                onRefresh = { news.refresh() },
                indicator = {
                    Indicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        isRefreshing = isRefreshing,
                        state = refreshState,
                    )
                },
                contentAlignment = Alignment.Center
            ) {
                when (uiState) {
                    ArticlesUiState.Empty -> {
                        Text("No articles found")
                        OutlinedButton(onClick = { news.retry() }) {
                            Text("Retry")
                        }
                    }

                    is ArticlesUiState.Error -> {
                        Text("error")
                        OutlinedButton(onClick = { news.retry() }) {
                            Text("Retry")
                        }
                    }

                    ArticlesUiState.Idle -> {
                        Text("Idle")
                    }

                    ArticlesUiState.Loading -> {
                        ShimmerEffect()
                    }

                    is ArticlesUiState.Success -> {
                        ArticlesList(
                            articles = news,
                            onClick = navigateToDetail
                        )
                    }
                }
            }
        }
    }
}