package karel.hudera.novak.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import karel.hudera.novak.data.remote.dto.primary.ApiArticle
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class Article(
    val author: String,
    val title: String,
    val content: String,
    val date: String,
    val url: String,
    val imageUrl: String
) : Parcelable

fun ApiArticle.toArticle(): Article {
    return Article(
        author = author ?: "Unknown Author",
        title = title ?: "Untitled",
        content = content ?: "No content available",
        date = publishedAt ?: "Unknown Date",
        url = url.orEmpty(),
        imageUrl = urlToImage.orEmpty()
    )
}