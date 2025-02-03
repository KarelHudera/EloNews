package karel.hudera.novak.data.remote.dto.primary


import kotlinx.serialization.Serializable

@Serializable
data class ApiSource(
    val id: String?,
    val name: String
)