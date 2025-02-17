package karel.hudera.novak.presentation.mainActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import karel.hudera.novak.presentation.navigation.NavGraph
import karel.hudera.novak.presentation.theme.NovaKTheme

/**
 * The main entry point of the application.
 *
 * This activity sets up the app's UI, manages the splash screen lifecycle, and initializes
 * navigation. It utilizes [MainViewModel] to determine the start destination of the app.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /** ViewModel responsible for managing the app's main navigation and splash screen state. */
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enables edge-to-edge display for a seamless UI experience.
        enableEdgeToEdge()

        // Installs and configures the splash screen, ensuring it remains until the ViewModel signals readiness.
        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.splashCondition.value }
        }

        // Sets the content of the activity using Jetpack Compose.
        setContent {
            NovaKTheme {
                // Initializes the navigation graph with the determined start destination.
                NavGraph(startDestination = viewModel.startDestination.value)
            }
        }
    }
}