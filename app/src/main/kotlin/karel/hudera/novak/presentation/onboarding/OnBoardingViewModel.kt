package karel.hudera.novak.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import karel.hudera.novak.domain.usecases.app_entry.AppEntryUseCases
import karel.hudera.novak.presentation.onboarding.components.pagesList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing the onboarding flow.
 *
 * This ViewModel tracks the user's progress through the onboarding screens and allows
 * saving the app entry state to indicate that onboarding has been completed.
 *
 * @param appEntryUseCases Use case for managing app entry preferences.
 */
@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val appEntryUseCases: AppEntryUseCases
) : ViewModel() {

    /**
     * The current page in the onboarding flow.
     *
     * This state is updated when the user navigates between onboarding screens.
     */
    private val _currentPage = MutableStateFlow(0)

    /** Publicly exposed state representing the current onboarding page. */
    val currentPage: StateFlow<Int> = _currentPage

    /** The list of onboarding pages. */
    val pages = pagesList

    /**
     * Saves the user's onboarding completion state.
     *
     * This method marks onboarding as completed, so the user is not shown the onboarding
     * screens again when reopening the app.
     */
    fun saveUserEntry() {
        viewModelScope.launch {
            appEntryUseCases.saveAppEntry()
        }
    }

    /**
     * Updates the current page in the onboarding flow.
     *
     * @param page The new page index.
     */
    fun updatePage(page: Int) {
        _currentPage.value = page
    }

    /**
     * Indicates whether the user is on the last onboarding page.
     */
    val isLastPage: Boolean
        get() = _currentPage.value == pages.size - 1
}