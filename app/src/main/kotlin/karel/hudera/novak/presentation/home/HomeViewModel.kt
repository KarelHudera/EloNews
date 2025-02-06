package karel.hudera.novak.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import karel.hudera.novak.domain.model.Article
import karel.hudera.novak.domain.usecases.news.NewsUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases
) : ViewModel() {

    private val _news = MutableStateFlow<PagingData<Article>>(PagingData.empty())
    val news: StateFlow<PagingData<Article>> = _news.asStateFlow()

    init {
        viewModelScope.launch {
            newsUseCases.getNews(
                sources = listOf( // TODO: hardcoded sources
                    "bbc-news",
                    "abc-news",
                    "al-jazeera-english",
                    "argaam",
                    "cnn",
                    "inancial-post",
                    "google-news-is",
                    "google-news-sa",
                    "rt",
                    "sabq",
                    "xinhua-net",
                    "ynet"
                )
            ).cachedIn(viewModelScope)
                .collect { pagingData ->
                    _news.value = pagingData
                }
        }
    }
}