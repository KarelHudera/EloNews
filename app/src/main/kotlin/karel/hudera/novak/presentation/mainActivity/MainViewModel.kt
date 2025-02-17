package karel.hudera.novak.presentation.mainActivity

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import karel.hudera.novak.domain.usecases.app_entry.AppEntryUseCases
import karel.hudera.novak.presentation.navigation.Route
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * ViewModel responsible for managing the app's main navigation and splash screen logic.
 *
 * It determines whether the app should start from the home screen or the onboarding screen
 * based on a stored preference. Additionally, it controls the visibility of the splash screen.
 *
 * @param appEntryUseCases Use case for retrieving the app entry preference.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    appEntryUseCases: AppEntryUseCases
) : ViewModel() {

    /**
     * Controls whether the splash screen is visible.
     *
     * Initially set to `true` and updated to `false` after determining the start destination.
     */
    private val _splashCondition = mutableStateOf(true)

    /** Publicly exposed state of the splash screen visibility. */
    val splashCondition: State<Boolean> = _splashCondition

    /**
     * Holds the start destination of the app.
     *
     * Defaults to the onboarding screen and is updated based on stored user preferences.
     */
    private val _startDestination = mutableStateOf(Route.AppStartNavigation.route)

    /** Publicly exposed state of the app's start destination. */
    val startDestination: State<String> = _startDestination

    init {
        appEntryUseCases.readAppEntry().onEach { shouldStartFromHomeScreen ->
            // Determine the start destination based on user preference
            _startDestination.value = if (shouldStartFromHomeScreen) {
                Route.NewsNavigation.route
            } else {
                Route.AppStartNavigation.route
            }

            // Delay to prevent a brief flash of the onboarding screen
            delay(200)

            // Hide splash screen
            _splashCondition.value = false
        }.launchIn(viewModelScope)
    }
}