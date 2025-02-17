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

/**
 * ViewModel responsible for fetching and managing articles data.
 *
 * This ViewModel handles the fetching of articles from a set of news sources using paging.
 * It also manages the UI state, including loading, error, success, and empty states.
 *
 * @param newsUseCases The use case for fetching articles from different news sources.
 */
@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases
) : ViewModel() {

    /**
     * The current UI state representing the loading, success, or error states of the articles.
     */
    private val _uiState = MutableStateFlow<ArticlesUiState>(ArticlesUiState.Idle)
    val uiState: StateFlow<ArticlesUiState> = _uiState.asStateFlow()

    /**
     * The current list of articles, represented by [PagingData].
     */
    private val _articles = MutableStateFlow<PagingData<Article>>(PagingData.empty())
    val articles: StateFlow<PagingData<Article>> = _articles.asStateFlow()

    init {
        fetchArticles()
    }

    /**
     * Fetches articles from multiple news sources and updates the [_articles] state.
     *
     * This method uses the [newsUseCases.getNews] to get paged articles from the news sources,
     * and caches the results in the ViewModel's scope to retain data during configuration changes.
     */
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

    /**
     * Handles the paging state, updating the [uiState] based on the load state of the articles.
     *
     * This method is used to show appropriate loading, error, or success states based on the current
     * state of the paging items.
     *
     * @param articles The [LazyPagingItems] representing the paged articles.
     */
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

/**
 * Sealed interface representing different states of the articles UI.
 *
 * The [ArticlesUiState] interface is used to represent the various states that the UI can be in
 * while fetching or displaying articles.
 * - [Idle] represents the initial or idle state before data is loaded.
 * - [Loading] represents the state when articles are being loaded.
 * - [Success] indicates that articles are successfully loaded.
 * - [Error] indicates that there was an error loading articles.
 * - [Empty] represents the state when no articles are available.
 *
 * Note: [Success] and [Error] states do not require parameters, as the articles are already
 * exposed to the UI via the [articles] state. The UI should update based on the availability
 * of articles and the current load state.
 */
sealed interface ArticlesUiState {
    data object Idle : ArticlesUiState
    data object Loading : ArticlesUiState
    data object Success : ArticlesUiState
    data object Error : ArticlesUiState
    data object Empty : ArticlesUiState
}