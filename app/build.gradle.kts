plugins {
    alias(libs.plugins.android.application) // Applies the Android application plugin, enabling Android app development.
    alias(libs.plugins.kotlin.android) // Enables Kotlin support for Android projects.
    alias(libs.plugins.kotlin.compose) // Adds Jetpack Compose support for building modern UI in a declarative way.
    alias(libs.plugins.kotlin.ksp) // Enables Kotlin Symbol Processing (KSP) for faster and more efficient annotation processing.
    alias(libs.plugins.hilt.android) // Integrates Hilt for Dependency Injection, simplifying DI in Android applications.
    alias(libs.plugins.kotlinx.serialization) // Adds Kotlin Serialization support for handling JSON and other data formats.
    alias(libs.plugins.kotlin.parcelize) // Enables `@Parcelize` for automatically generating Parcelable implementations in Kotlin.
}

android {
    namespace = "karel.hudera.novak"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "karel.hudera.novak"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // Set to true in production to obfuscate the code.
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Core AndroidX dependencies
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)

    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom)) // BOM for Compose versions
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3) // Material 3

    // Compose icons (https://feathericons.com/)
    implementation(libs.icons.feather)

    // Navigation
    implementation(libs.androidx.navigation)

    // Dependency Injection (Hilt)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation)
    ksp(libs.hilt.compiler) // KSP compiler for Hilt

    // Splash Screen API
    implementation(libs.androidx.core.splashscreen)

    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // DataStore (for local storage)
    implementation(libs.androidx.datastore)

    // Networking (Retrofit + Logging)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Kotlin Serialization library for converting JSON data into Kotlin objects
    implementation(libs.kotlinx.serialization.json)

    // Paging (for infinite scrolling)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    // Image Loading (Coil for Compose)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test)

    // Debugging tools
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}