package karel.hudera.novak.presentation.articles.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import dagger.hilt.android.EntryPointAccessors
import karel.hudera.novak.R
import karel.hudera.novak.di.AppModule
import karel.hudera.novak.domain.model.Article
import karel.hudera.novak.presentation.common.shimmerEffect
import karel.hudera.novak.presentation.theme.NovaKTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


private val COLUMN_HEIGHT = 100.dp

@Composable
fun ArticleCard(
    modifier: Modifier = Modifier,
    apiArticle: Article,
    onClick: () -> Unit
) {
    // Context for AsyncImage loading
    val imageLoader = rememberImageLoader()

    Row(
        modifier = modifier
            .clickable { onClick() }
            .padding(16.dp) // Add padding around the entire row
    ) {
        // AsyncImage with placeholder
        AsyncImage(
            model = apiArticle.imageUrl,
            contentDescription = apiArticle.title,
            imageLoader = imageLoader,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.news),
            error = painterResource(R.drawable.news),
            modifier = Modifier
                .size(COLUMN_HEIGHT)
                .clip(RoundedCornerShape(8.dp))
        )

        // Column for text content
        Column(
            modifier = Modifier
                .padding(start = 16.dp) // Space between image and text
                .fillMaxWidth()
                .height(COLUMN_HEIGHT),
            verticalArrangement = Arrangement.spacedBy(4.dp) // Add spacing between texts
        ) {
            Text(
                text = apiArticle.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = apiArticle.content,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.alpha(0.7f)
            )

            // Spacer to push date text to the bottom
            Spacer(modifier = Modifier.weight(1f)) // Takes up remaining space

            Text(
                text = formatPublishedDate(apiArticle.date),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .alpha(0.7f)
                    .align(Alignment.Start)
            )
        }


    }
    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ArticleCardPreview() {
    NovaKTheme {
        ArticleCard(
            apiArticle = Article(
                author = "Bbc News",
                title = "Belgian police hunt for gunmen in Brussels underground",
                content = "The spokeswoman said there were no injuries in the shooting. Both the local police and federal railway police are searching the area.\\r\\nPolice are looking for \\\"a small group of people, probably two or… [+1084 chars]",
                date = "2025-02-05T09:51:53Z",
                url = "https://www.bbc.co.uk/news/articles/cn4mvl1ngk1o",
                imageUrl = "https://ichef.bbci.co.uk/ace/branded_news/1200/cpsprodpb/069d/live/16ca3950-e3a7-11ef-8450-ff58a15d40df.jpg",
            ),
            onClick = {}
        )
    }
}

// Helper function to format date
fun formatPublishedDate(date: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val parsedDate: Date = inputFormat.parse(date) ?: Date()
        outputFormat.format(parsedDate)
    } catch (e: Exception) {
        date // fallback to original if parsing fails
    }
}

@Composable
fun rememberImageLoader(): ImageLoader {
    val context = LocalContext.current
    return remember {
        EntryPointAccessors.fromApplication(context, AppModule.ImageLoaderEntryPoint::class.java)
            .imageLoader()
    }
}

@Composable
fun ArticleCardShimmerEffect(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(COLUMN_HEIGHT)
                .clip(MaterialTheme.shapes.medium)
                .shimmerEffect()
        )
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(COLUMN_HEIGHT)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .height(26.dp)
                    .shimmerEffect()
            )
            Spacer(Modifier.size(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .height(26.dp)
                    .shimmerEffect()
            )

            Spacer(modifier = Modifier.weight(1f)) // Takes up remaining space

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .clip(RoundedCornerShape(8.dp))
                    .height(12.dp)
                    .shimmerEffect()
            )
        }
    }
    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
}