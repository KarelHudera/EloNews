package karel.hudera.novak.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import karel.hudera.novak.domain.model.Article
import karel.hudera.novak.domain.model.toArticle

class NewsPagingSource(
    private val newsApi: NewsApi,
    private val sources: String
) : PagingSource<Int, Article>() {

    // Used for debugging or logging purposes.
    private val TAG = NewsPagingSource::class.java.simpleName

    // Simplified getRefreshKey for better readability and reliability
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        // Refresh key should be based on the anchor position. If there is a next or previous page, it gives better context.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    private var totalNewsCount = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1 // Default to page 1 if no key is provided.
        return try {

            // Call the API and get the response.
            val newsResponse = newsApi.getNews(sources = sources, pageNumber = page)

            val articles = newsResponse.articles
                .distinctBy { it.title } // Remove duplicates
                .map { it.toArticle() } // Map ApiArticle to Article.

            // Log the current articles for debugging purposes.
            Log.d(TAG, "Loaded ${articles.size} articles for page $page")

            // Update total news count based on the response.
            totalNewsCount = newsResponse.articles.size

            // Calculate the next and previous pages based on the total count.
            val nextKey = if (articles.size < newsResponse.totalResults) page + 1 else null
            val prevKey = if (page > 1) page - 1 else null

            // Return the paged data.
            LoadResult.Page(
                data = articles,
                prevKey = prevKey,
                nextKey = nextKey
            )

        } catch (e: Exception) {
            // Log and return the error if something goes wrong.
            Log.e(TAG, "Error loading news for page $page", e)
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}