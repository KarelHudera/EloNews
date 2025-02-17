package karel.hudera.novak.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import karel.hudera.novak.data.remote.dto.primary.ApiArticle
import kotlinx.parcelize.Parcelize

/**
 * Represents an article with relevant details such as author, title, content, publication date,
 * URL, and image URL.
 *
 * This class is marked as [Parcelable] to allow for efficient passing of article data between
 * different Android components, such as Activities or Fragments.
 *
 * @property author The author of the article. If not available, defaults to "Unknown Author".
 * @property title The title of the article. If not available, defaults to "Untitled".
 * @property content The main content of the article. If not available, defaults to "No content available".
 * @property date The publication date of the article. If not available, defaults to "Unknown Date".
 * @property url The URL link to the article.
 * @property imageUrl The URL link to the image associated with the article.
 */
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

/**
 * Converts an [ApiArticle] object (from the remote DTO layer) to an [Article] domain model.
 *
 * This extension function is used to map the raw data coming from the API to the domain model
 * used by the app. It ensures that missing or `null` values in the [ApiArticle] are handled gracefully
 * by providing default values.
 *
 * @return The corresponding [Article] object.
 */
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