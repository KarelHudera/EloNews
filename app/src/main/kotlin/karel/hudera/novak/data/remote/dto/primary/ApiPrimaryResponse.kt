package karel.hudera.novak.data.remote.dto.primary


import kotlinx.serialization.Serializable

@Serializable
data class ApiPrimaryResponse(
    val articles: List<ApiArticle>,
    val status: String,
    val totalResults: Int
)