package karel.hudera.novak.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import karel.hudera.novak.domain.usecases.news.NewsUseCases
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases
) : ViewModel() {
    val news = newsUseCases.getNews(
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
}