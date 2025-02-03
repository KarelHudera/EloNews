package karel.hudera.novak.data.remote.dto.primary


import kotlinx.serialization.Serializable

@Serializable
data class ApiArticle(
    val author: String,
    val content: String?,
    val description: String?,
    val publishedAt: String,
    val apiSource: ApiSource,
    val title: String,
    val url: String,
    val urlToImage: String?
)