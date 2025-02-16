package karel.hudera.novak.presentation.search

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import karel.hudera.novak.domain.model.Article
import karel.hudera.novak.presentation.common.ArticlesList
import karel.hudera.novak.presentation.navigation.FAB_ANIMATION_KEY

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.SearchScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: SearchViewModel = hiltViewModel(),
    navigateToDetail: (Article) -> Unit
) {
    val articles = viewModel.articles.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery

    Scaffold(
        Modifier.sharedBounds(
            sharedContentState = rememberSharedContentState(key = FAB_ANIMATION_KEY),
            animatedVisibilityScope = animatedVisibilityScope
        )
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())

        ) {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    label = { Text("Search articles") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        viewModel.onSearchQueryChanged(searchQuery)
                    })
                )
                ArticlesList(
                    articles = articles,
                    onClick = navigateToDetail
                )
            }
        }
    }
}