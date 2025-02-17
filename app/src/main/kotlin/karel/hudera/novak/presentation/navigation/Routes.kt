package karel.hudera.novak.presentation.navigation

import androidx.navigation.NamedNavArgument


/**
 * A sealed class representing the different routes in the app's navigation system.
 *
 * Each route corresponds to a specific screen or destination within the app. The route's
 * `route` string is used for navigation, and `arguments` can be added to pass data
 * between destinations if needed.
 */
sealed class Route(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object AppStartNavigation : Route(route = "appStartNavigation")

    data object OnBoardingScreen : Route(route = "onBoardingScreen")

    // After the user has seen the onboarding screen, the app will navigate to the news screen.
    data object NewsNavigation : Route(route = "newsNavigation")

    data object ArticlesScreen : Route(route = "articlesScreen")

    data object SearchScreen : Route(route = "searchScreen")

    data object BookmarkScreen : Route(route = "bookmarkScreen")

    data object SettingsScreen : Route(route = "settingsScreen")

    data object DetailScreen : Route(route = "detailsScreen")
}