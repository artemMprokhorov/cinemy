plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.compose.compiler)
    id("kotlin-parcelize")
}

android {
    namespace = "org.studioapp.cinemy"
    compileSdk = 36

    defaultConfig {
        applicationId = "org.studioapp.cinemy"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "2.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    // SIMPLIFIED: Only 2 build types
    buildTypes {
        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            buildConfigField("String", "BUILD_TYPE", "\"DEBUG\"")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BUILD_TYPE", "\"RELEASE\"")
        }
    }

    // SIMPLIFIED: Only 2 flavors  
    flavorDimensions += "environment"
    productFlavors {
        create("dummy") {
            dimension = "environment"
            applicationIdSuffix = ".dummy"
            versionNameSuffix = "-dummy"

            buildConfigField("Boolean", "USE_MOCK_DATA", "true")
            buildConfigField("String", "MCP_SERVER_URL", "\"\"")
            buildConfigField("String", "FLAVOR_NAME", "\"Dummy\"")
            buildConfigField("String", "TMDB_IMAGE_BASE_URL", "\"https://image.tmdb.org/t/p/\"")

            resValue("string", "app_name", "Cinemy Dummy")
        }

        create("prod") {
            dimension = "environment"
            applicationIdSuffix = ".prod"
            versionNameSuffix = "-prod"

            buildConfigField("Boolean", "USE_MOCK_DATA", "false")
            buildConfigField(
                "String",
                "MCP_SERVER_URL",
                "\"https://grand-beagle-reliably.ngrok-free.app/webhook/tmdbai-enhanced\""
            )
            buildConfigField("String", "FLAVOR_NAME", "\"Production\"")
            buildConfigField("String", "TMDB_IMAGE_BASE_URL", "\"https://image.tmdb.org/t/p/\"")

            resValue("string", "app_name", "Cinemy")
        }
    }

    // Filter out dummyRelease variant to have only 3 variants total
    variantFilter {
        if (name == "dummyRelease") {
            ignore = true
        }
    }

    // Remove all signing configs for simplicity
    // Remove all version management complexity
    // Remove all GitHub Actions complexity

    buildFeatures {
        compose = true
        buildConfig = true
    }

    aaptOptions {
        noCompress("tflite")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    // Jetpack Compose BOM - Latest version
    implementation(platform(libs.androidx.compose.bom))

    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Jetpack Compose
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("androidx.compose.material:material-icons-extended")

    // Navigation Compose 2.9+ with type-safe routes (latest stable with AGP 8.6.0)
    implementation(libs.androidx.navigation.compose)
    implementation("androidx.navigation:navigation-runtime-ktx:2.9.3")

    // ViewModel & State Management
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // Dependency Injection - Koin (using version catalog)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.core)

    // Coroutines & Reactive Programming (using version catalog)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // Networking - Ktor for MCP protocol (using version catalog)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.auth)

    // Image Loading - Coil (using version catalog)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.coil.svg)

    // JSON Processing - Kotlinx Serialization for MCP (using version catalog)
    implementation(libs.kotlinx.serialization.json)
    // Keep Gson for backward compatibility during migration
    implementation("com.google.code.gson:gson:2.10.1")

    // Date/Time
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")

    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // SystemUI Controller (using version catalog)
    implementation(libs.accompanist.systemuicontroller)

    // Permissions (using version catalog)
    implementation(libs.accompanist.permissions)

    // Pull to Refresh
    implementation("androidx.compose.material:material:1.6.1")

    // Paging for movie lists (using version catalog)
    implementation(libs.paging.runtime.ktx)
    implementation(libs.paging.compose)

    // DataStore for preferences (using version catalog)
    implementation(libs.datastore.preferences)

    // Timber for logging (using version catalog)
    implementation(libs.timber)

    // TensorFlow Lite for ML sentiment analysis
    implementation("org.tensorflow:tensorflow-lite-support:0.4.4")

    // Testing dependencies (using version catalog)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit4)
    testImplementation("androidx.arch.core:core-testing:2.2.0")

    // Android testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation("androidx.navigation:navigation-testing:2.9.3")
    androidTestImplementation(libs.koin.test)
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")

    // Debug dependencies
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12")
}