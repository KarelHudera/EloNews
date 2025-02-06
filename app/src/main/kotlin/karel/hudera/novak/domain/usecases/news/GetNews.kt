package karel.hudera.novak.domain.usecases.news

import androidx.paging.PagingData
import karel.hudera.novak.domain.model.Article
import karel.hudera.novak.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class GetNews(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(sources: List<String>): Flow<PagingData<Article>> {
        return newsRepository.getNews(sources = sources)
    }
}