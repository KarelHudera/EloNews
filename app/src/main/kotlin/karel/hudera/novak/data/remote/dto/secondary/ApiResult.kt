package karel.hudera.novak.data.remote.dto.secondary


import kotlinx.serialization.Serializable

@Serializable
data class ApiResult(
    val aiOrg: String,
    val aiRegion: String,
    val aiTag: String,
    val articleId: String,
    val category: List<String>,
    val content: String,
    val country: List<String>,
    val creator: List<String>?,
    val description: String,
    val duplicate: Boolean,
    val imageUrl: String,
    val keywords: List<String>?,
    val language: String,
    val link: String,
    val pubDate: String,
    val pubDateTZ: String,
    val sentiment: String,
    val sentimentStats: String,
    val sourceIcon: String,
    val sourceId: String,
    val sourceName: String,
    val sourcePriority: Int,
    val sourceUrl: String,
    val title: String,
    val videoUrl: String?
)