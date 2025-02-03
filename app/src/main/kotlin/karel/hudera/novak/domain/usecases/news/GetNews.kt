package karel.hudera.novak.domain.usecases.news

import androidx.paging.PagingData
import karel.hudera.novak.data.remote.dto.primary.ApiArticle
import karel.hudera.novak.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class GetNews(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(sources: List<String>): Flow<PagingData<ApiArticle>> {
        return newsRepository.getNews(sources = sources)
    }
}