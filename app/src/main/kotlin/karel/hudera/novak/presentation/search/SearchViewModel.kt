package karel.hudera.novak.presentation.search

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import karel.hudera.novak.domain.model.Article
import karel.hudera.novak.domain.usecases.news.NewsUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases
) : ViewModel() {

    private val _articles = MutableStateFlow<PagingData<Article>>(PagingData.empty())
    val articles: StateFlow<PagingData<Article>> = _articles.asStateFlow()

    private val _searchQuerry = mutableStateOf("")
    val searchQuery: State<String> = _searchQuerry

    private var searchJob: Job? = null

    fun onSearchQueryChanged(query: String) {
        Log.d("SearchViewModel", "User typed: $query")
        _searchQuerry.value = query

        searchJob?.cancel() // Cancel previous job to debounce input
        searchJob = viewModelScope.launch {
            delay(100) // Debounce to avoid too many API calls
            if (query.isNotEmpty()) {
                searchNews(query)
            }
        }
    }

    private fun searchNews(query: String) {
        Log.d("SearchViewModel", "Searching for: $query")

        viewModelScope.launch {
            newsUseCases.searchNews(
                searchQuery = query,
                sources = listOf(
                    "bbc-news",
                    "abc-news",
                    "cnn",
                    "financial-post"
                )
            ).cachedIn(viewModelScope)
                .collect { pagingData ->
                    Log.d("SearchViewModel", "Received $pagingData")
                    _articles.value = pagingData
                }
        }
    }
}