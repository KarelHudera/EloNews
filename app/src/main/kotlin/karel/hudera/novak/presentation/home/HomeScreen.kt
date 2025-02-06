package karel.hudera.novak.presentation.home

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import karel.hudera.novak.domain.model.Article
import karel.hudera.novak.presentation.common.ArticlesList


@Composable
fun HomeScreen(articles: LazyPagingItems<Article>) {

    val titles by remember {
        derivedStateOf {
            if (articles.itemCount > 10) {
                articles.itemSnapshotList.items
                    .slice(IntRange(start = 0, endInclusive = 9))
                    .joinToString(separator = " \uD83D\uDFE5 ") { it.title }
            } else {
                ""
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Text(
            text = titles, modifier = Modifier
                .fillMaxWidth()
                .basicMarquee(), fontSize = 12.sp,
        )


        ArticlesList(
            articles = articles,
            onClick = {
                //TODO: Navigate to Details Screen
            }
        )
    }
}