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

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val appEntryUseCases: AppEntryUseCases
) : ViewModel() {

    // State: Current page in the onboarding flow
    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage

    // Constant: Total number of pages
    val pages = pagesList

    // Save user entry after onboarding is complete
    fun saveUserEntry() {
        viewModelScope.launch {
            appEntryUseCases.saveAppEntry()
        }
    }

    // Update the current page
    fun updatePage(page: Int) {
        _currentPage.value = page
    }

    // Helper to check if it's the last page
    val isLastPage: Boolean
        get() = _currentPage.value == pages.size - 1
}