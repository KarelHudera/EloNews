package karel.hudera.novak.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import karel.hudera.novak.data.remote.NewsApi
import karel.hudera.novak.data.remote.NewsPagingSource
import karel.hudera.novak.data.remote.SearchNewsPagingSource
import karel.hudera.novak.domain.model.Article
import karel.hudera.novak.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

class NewsRepositoryImpl(
    private val newsApi: NewsApi
) : NewsRepository {
    override suspend fun getNews(
        sources: List<String>
    ): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                NewsPagingSource(
                    newsApi = newsApi,
                    sources = sources.joinToString(separator = ",")
                )
            }
        ).flow
    }

    override suspend fun searchNews(
        searchQuery: String,
        sources: List<String>
    ): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                SearchNewsPagingSource(
                    searchQuery = searchQuery,
                    newsApi = newsApi,
                    sources = sources.joinToString(separator = ",")
                )
            }
        ).flow
    }
}