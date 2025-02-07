package karel.hudera.novak.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import karel.hudera.novak.domain.model.Article
import karel.hudera.novak.presentation.common.ArticlesList
import kotlinx.coroutines.delay


@Composable
fun HomeScreen(articles: LazyPagingItems<Article>) {

    val titles by remember {
        derivedStateOf {
            if (articles.itemCount > 10) {
                articles.itemSnapshotList.items
                    .slice(IntRange(start = 0, endInclusive = 9))
                    .joinToString(separator = " \uD83D\uDFE5 ") { it.title }
            } else {
                null
            }
        }
    }

    var query by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        SearchBar(
            query = query,
            onQueryChange = { query = it },
            onSearch = {}
        )

        titles?.let {
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

        HorizontalDivider(
            color = Color.Transparent,
            modifier = Modifier.shadow(1.dp)
        )

        ArticlesList(
            articles = articles,
            onClick = {
                //TODO: Navigate to Details Screen
            }
        )
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        placeholder = { Text("Search...") },
        singleLine = true
    )
}

@Composable
fun AnimatedTitle(titles: String?) {
    var isVisible by remember { mutableStateOf(false) }

    // Show animation after a small delay
    LaunchedEffect(titles) {
        isVisible = false
        delay(300) // Optional: small delay before showing
        isVisible = true
    }

    titles?.let {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(500)) + slideInVertically { it / 2 },
            exit = fadeOut(animationSpec = tween(300)) + slideOutVertically { it / 2 }
        ) {
            Text(
                text = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee(),
                fontSize = 12.sp,
            )
        }
    }
}