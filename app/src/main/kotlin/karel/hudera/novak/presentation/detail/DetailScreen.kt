package karel.hudera.novak.presentation.detail

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.Bookmark
import compose.icons.feathericons.Globe
import compose.icons.feathericons.Share2
import karel.hudera.novak.R
import karel.hudera.novak.domain.model.Article
import karel.hudera.novak.presentation.articles.components.rememberImageLoader

@Composable
fun DetailScreen(
    article: Article,
    backNavigation: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            DetailTopBar(
                onBackClick = backNavigation,
                onBookmarkClick = { // TODO: Implement bookmarking
                },
                articleUrl = article.url,
                context = context
            )

        }
    )
    { innerPadding ->

        val scrollState = rememberScrollState()
        val imageLoader = rememberImageLoader()


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding)
        ) {
            AsyncImage(
                model = article.imageUrl,
                contentDescription = article.title,
                imageLoader = imageLoader,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.news),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
            )

        }

    }

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DetailTopBar(
    onBackClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    articleUrl: String,
    context: Context
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            ActionIconButton(onBackClick, FeatherIcons.ArrowLeft, "Back")
        },
        actions = {
            ActionIconButton(
                onClick = onBookmarkClick,
                icon = FeatherIcons.Bookmark,
                description = "Bookmark"
            )
            ActionIconButton(
                onClick = { shareArticle(context, articleUrl) },
                icon = FeatherIcons.Share2,
                description = "Share"
            )
            ActionIconButton(
                onClick = { openInBrowser(context, articleUrl) },
                icon = FeatherIcons.Globe,
                description = "Open in Browser"
            )
        }
    )
}


/**
 * A generic icon button used in the top bar.
 *
 * @param onClick Action to perform when the button is clicked.
 * @param icon The Feather icon to display.
 * @param description Content description for accessibility.
 */
@Composable
fun ActionIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    description: String
) {
    IconButton(onClick) {
        Icon(
            painter = rememberVectorPainter(icon),
            contentDescription = description
        )
    }
}

/**
 * Shares the given article URL using available sharing apps.
 */
private fun shareArticle(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
    }
    context.startActivitySafely(intent)
}

/**
 * Opens the given URL in a web browser.
 */
private fun openInBrowser(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    context.startActivitySafely(intent)
}

/**
 * Starts an activity safely, checking if an app is available to handle the intent.
 */
private fun Context.startActivitySafely(intent: Intent) {
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}