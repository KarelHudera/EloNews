package karel.hudera.novak.domain.repository

import androidx.paging.PagingData
import karel.hudera.novak.domain.model.Article
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

interface NewsRepository {
    suspend fun getNews(sources: List<String>): Flow<PagingData<Article>>
    suspend fun searchNews(searchQuery: String, sources: List<String>): Flow<PagingData<Article>>
}