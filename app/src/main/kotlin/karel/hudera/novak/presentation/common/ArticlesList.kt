package karel.hudera.novak.presentation.common

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import karel.hudera.novak.domain.model.Article
import karel.hudera.novak.presentation.articles.components.ArticleCard
import karel.hudera.novak.presentation.articles.components.ArticleCardShimmerEffect

@Composable
fun ArticlesList(
    modifier: Modifier = Modifier,
    articles: LazyPagingItems<Article>,
    onClick: (Article) -> Unit
) {
    val loadState = articles.loadState

    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        items(
            count = articles.itemCount,
        ) {
            articles[it]?.let { article ->
                ArticleCard(apiArticle = article, onClick = { onClick(article) })
            }
        }

        // Show loading text while still in loading state
        if (loadState.append is LoadState.Loading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    Text("Loading...", modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

fun Modifier.shimmerEffect() = composed {
    val transition = rememberInfiniteTransition()
    val alpha = transition.animateFloat(
        initialValue = 0.2f, targetValue = 0.9f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        )
    ).value
    background(color = Color.LightGray.copy(alpha = alpha))
}

@Composable
fun ShimmerEffect() {
    LazyColumn  {
        items(10) {
            ArticleCardShimmerEffect(
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}