package karel.hudera.novak.data.remote


import karel.hudera.novak.data.remote.dto.primary.ApiPrimaryResponse
import karel.hudera.novak.util.Keys.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface that defines API endpoints for fetching news articles from a remote news service.
 * It includes methods for fetching general news as well as searching news articles based on a query.
 *
 * All requests use the `API_KEY` from the [Keys] class by default for authentication.
 */
interface NewsApi {

    /**
     * Fetches a paginated list of news articles from a specific source.
     *
     * @param pageNumber The page number for pagination.
     * @param sources A comma-separated list of news sources (e.g., "bbc-news", "cnn").
     * @param apiKey The API key used for authentication (default is set to [API_KEY]).
     * @return [ApiPrimaryResponse] containing the response data from the news API.
     */
    @GET("everything")
    suspend fun getNews(
        @Query("page") pageNumber: Int,
        @Query("sources") sources: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): ApiPrimaryResponse

    /**
     * Searches for news articles based on a query.
     *
     * @param searchQuery The search query used to filter the news articles.
     * @param pageNumber The page number for pagination.
     * @param sources A comma-separated list of news sources (e.g., "bbc-news", "cnn").
     * @param apiKey The API key used for authentication (default is set to [API_KEY]).
     * @return [ApiPrimaryResponse] containing the search results.
     */
    @GET("everything")
    suspend fun searchNews(
        @Query("q") searchQuery: String,
        @Query("page") pageNumber: Int,
        @Query("sources") sources: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): ApiPrimaryResponse
}