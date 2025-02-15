package karel.hudera.novak.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import karel.hudera.novak.domain.model.Article
import karel.hudera.novak.presentation.articles.ArticlesScreen
import karel.hudera.novak.presentation.articles.ArticlesViewModel
import karel.hudera.novak.presentation.bookmark.BookmarkScreen
import karel.hudera.novak.presentation.detail.DetailScreen
import karel.hudera.novak.presentation.onboarding.OnBoardingScreen
import karel.hudera.novak.presentation.onboarding.OnBoardingViewModel
import karel.hudera.novak.presentation.settings.SettingsScreen

@Composable
fun NavGraph(
    startDestination: String
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.OnBoardingScreen.route
        ) {
            composable(route = Route.OnBoardingScreen.route) {
                val viewModel: OnBoardingViewModel = hiltViewModel()
                OnBoardingScreen(viewModel = viewModel)
            }
        }

        navigation(
            route = Route.NewsNavigation.route,
            startDestination = Route.ArticlesScreen.route
        ) {
            composable(route = Route.ArticlesScreen.route) {
                val viewModel: ArticlesViewModel = hiltViewModel()


                val pagerState = rememberPagerState(
                    initialPage = 1, // Start at index 1 (ArticlesScreen)
                    pageCount = { 3 } // Total number of pages
                )

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                ) { pageIndex ->
                    when (pageIndex) {
                        0 -> BookmarkScreen()
                        1 -> ArticlesScreen(
                            navigateToDetail = { article ->
                                navigateToDetail(
                                    navController = navController,
                                    article = article
                                )
                            },
                            viewModel = viewModel
                        )

                        2 -> SettingsScreen()
                    }
                }
            }
            composable(route = Route.DetailScreen.route) {
                navController.previousBackStackEntry?.savedStateHandle?.get<Article?>("article")
                    ?.let { article ->
                        DetailScreen(
                            article = article,
                            backNavigation = { navController.navigateUp() }
                        )
                    }
            }
            composable(route = Route.BookmarkScreen.route) {
                BookmarkScreen()
            }
            composable(route = Route.SettingsScreen.route) {
                SettingsScreen()
            }
        }
    }
}

fun navigateToDetail(navController: NavController, article: Article) {
    navController.currentBackStackEntry?.savedStateHandle?.set("article", article)
    navController.navigate(
        route = Route.DetailScreen.route
    )
}