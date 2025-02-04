package karel.hudera.novak.domain.repository

import androidx.paging.PagingData
import karel.hudera.novak.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(sources: List<String>): Flow<PagingData<Article>>
}