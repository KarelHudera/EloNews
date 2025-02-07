package karel.hudera.novak.data.remote.dto.secondary


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SecondaryApiResponse(
    val nextPage: String,
    val apiResults: List<ApiResult>,
    val status: String,
    val totalResults: Int
)
