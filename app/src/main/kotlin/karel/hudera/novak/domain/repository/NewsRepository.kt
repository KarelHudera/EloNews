package karel.hudera.novak.domain.repository

import androidx.paging.PagingData
import karel.hudera.novak.data.remote.dto.primary.ApiArticle
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(sources: List<String>): Flow<PagingData<ApiArticle>>
}