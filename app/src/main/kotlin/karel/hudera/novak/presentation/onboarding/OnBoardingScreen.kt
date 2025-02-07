package karel.hudera.novak.presentation.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import karel.hudera.novak.presentation.onboarding.components.NewsButton
import karel.hudera.novak.presentation.onboarding.components.NewsTextButton
import karel.hudera.novak.presentation.onboarding.components.OnBoardingPage
import karel.hudera.novak.presentation.onboarding.components.PagerIndicator
import kotlinx.coroutines.launch

@Composable
fun OnBoardingScreen(
    viewModel: OnBoardingViewModel
) {
    val currentPage by viewModel.currentPage.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = currentPage) { viewModel.pages.size }

    // Sync page changes with ViewModel
    LaunchedEffect(pagerState.currentPage) {
        viewModel.updatePage(pagerState.currentPage)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Horizontal Pager for onboarding pages
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { pageIndex ->
                OnBoardingPage(
                    page = viewModel.pages[pageIndex],
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Pager Indicator
                PagerIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(52.dp),
                    pagesSize = viewModel.pages.size,
                    selectedPage = currentPage
                )

                // Navigation Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // "Back" button (visible only if not on the first page)
                    if (currentPage > 0) {
                        NewsTextButton(
                            text = "Back",
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(currentPage - 1)
                                }
                            }
                        )
                    }

                    // "Next" or "Get Started" button
                    NewsButton(
                        text = if (viewModel.isLastPage) "Get Started" else "Next",
                        onClick = {
                            coroutineScope.launch {
                                if (viewModel.isLastPage) {
                                    viewModel.saveUserEntry() // Save user entry on last page
                                } else {
                                    pagerState.animateScrollToPage(currentPage + 1)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}