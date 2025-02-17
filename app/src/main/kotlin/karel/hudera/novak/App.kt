package karel.hudera.novak

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * The application class for the app.
 *
 * This class serves as the entry point for dependency injection using Hilt.
 * Annotating it with [HiltAndroidApp] triggers Hilt's code generation and
 * enables dependency injection throughout the application.
 */
@HiltAndroidApp
class App : Application()