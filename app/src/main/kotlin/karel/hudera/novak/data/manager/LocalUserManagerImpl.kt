package karel.hudera.novak.data.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import karel.hudera.novak.domain.manger.LocalUserManager
import karel.hudera.novak.util.Constants
import karel.hudera.novak.util.Constants.USER_SETTINGS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of the [LocalUserManager] interface for managing local user preferences.
 *
 * Uses Android's DataStore to persist and retrieve simple user settings (e.g., if the app entry has been completed).
 */
class LocalUserManagerImpl(
    private val context: Context
) : LocalUserManager {

    /**
     * Saves the user's app entry status (e.g., whether the user has seen the onboarding).
     *
     * This method updates the "APP_ENTRY" key in the preferences to true.
     */
    override suspend fun saveAppEntry() {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.APP_ENTRY] = true
        }
    }

    /**
     * Reads the current app entry status (whether the user has seen the onboarding).
     *
     * Returns a [Flow] of a Boolean indicating whether the app entry has been completed.
     */
    override fun readAppEntry(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.APP_ENTRY] ?: false
        }
    }
}

/** DataStore instance with a specific name for storing user preferences. */
private val readOnlyProperty = preferencesDataStore(name = USER_SETTINGS)

/** Extension property on [Context] that provides access to the DataStore for reading and writing preferences. */
val Context.dataStore: DataStore<Preferences> by readOnlyProperty

/**
 * Defines keys used in the DataStore for storing and retrieving preferences.
 *
 * Each key corresponds to a preference value in the DataStore.
 */
private object PreferenceKeys {
    val APP_ENTRY = booleanPreferencesKey(name = Constants.APP_ENTRY)
}