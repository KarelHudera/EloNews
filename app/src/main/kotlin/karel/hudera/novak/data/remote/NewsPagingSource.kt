package karel.hudera.novak.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import karel.hudera.novak.domain.model.Article
import karel.hudera.novak.domain.model.toArticle

/**
 * A [PagingSource] implementation for loading news articles from the [NewsApi] in a paginated manner.
 *
 * This class retrieves a list of articles from a remote API and returns them in pages. It supports
 * pagination by maintaining the current page and providing the next and previous page keys.
 *
 * @param newsApi The [NewsApi] instance used to fetch news data.
 * @param sources A string representing the source(s) of the news to be fetched (e.g., "bbc-news", "cnn").
 */
class NewsPagingSource(
    private val newsApi: NewsApi,
    private val sources: String
) : PagingSource<Int, Article>() {

    // Used for debugging or logging purposes.
    private val TAG = NewsPagingSource::class.java.simpleName

    /**
     * Returns the key for the next page based on the current state of the paging.
     *
     * The refresh key helps to reload the paging list at a specific position. In this case, it uses
     * the anchor position (current page) to determine the next or previous key.
     *
     * @param state The current [PagingState] containing information about the list's paging state.
     * @return The key of the next page or null if there is no next page.
     */
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        // Refresh key should be based on the anchor position. If there is a next or previous page, it gives better context.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    // Keeps track of the total number of articles in the API response.
    private var totalNewsCount = 0

    /**
     * Loads a page of news articles from the [NewsApi] and handles pagination.
     *
     * This function retrieves articles from the API, removes duplicates, and maps the response
     * into domain models. It also calculates the next and previous page keys based on the current page
     * and total results available in the response.
     *
     * @param params The parameters that include the current page (key) and load size.
     * @return A [LoadResult] containing the paged articles and pagination information (previous and next keys).
     */
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