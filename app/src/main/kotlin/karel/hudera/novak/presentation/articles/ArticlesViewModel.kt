package karel.hudera.novak.presentation.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import dagger.hilt.android.lifecycle.HiltViewModel
import karel.hudera.novak.domain.model.Article
import karel.hudera.novak.domain.usecases.news.NewsUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<ArticlesUiState>(ArticlesUiState.Idle)
    val uiState: StateFlow<ArticlesUiState> = _uiState.asStateFlow()

    private val _articles = MutableStateFlow<PagingData<Article>>(PagingData.empty())
    val articles: StateFlow<PagingData<Article>> = _articles.asStateFlow()

    init {
        fetchArticles()
    }

     private fun fetchArticles() {
        viewModelScope.launch {
            newsUseCases.getNews(
                sources = listOf( // TODO: Move sources to a repository or data source
                    "bbc-news",
                    "abc-news",
                    "cnn",
                    "financial-post"
                )
            ).cachedIn(viewModelScope)
                .collect { pagingData ->
                    _articles.value = pagingData
                }
        }
    }

    fun handlePagingState(articles: LazyPagingItems<Article>) {
        val loadState = articles.loadState

        when {
            loadState.refresh is LoadState.Loading -> {
                _uiState.value = ArticlesUiState.Loading
            }

            articles.itemCount == 0 -> {
                _uiState.value = ArticlesUiState.Empty
            }

            loadState.refresh is LoadState.Error -> {
                _uiState.value = ArticlesUiState.Error
            }

            else -> {
                _uiState.value = ArticlesUiState.Success
            }
        }
    }
}

sealed interface ArticlesUiState {
    data object Idle : ArticlesUiState
    data object Loading : ArticlesUiState
    data object Success : ArticlesUiState
    data object Error : ArticlesUiState
    data object Empty : ArticlesUiState
}