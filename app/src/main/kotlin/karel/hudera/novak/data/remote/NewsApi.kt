package karel.hudera.novak.data.remote


import karel.hudera.novak.data.remote.dto.primary.ApiPrimaryResponse
import karel.hudera.novak.util.Keys.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("everything")
    suspend fun getNews(
        @Query("page") pageNumber: Int,
        @Query("sources") sources: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): ApiPrimaryResponse
}