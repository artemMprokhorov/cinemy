import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinx.serialization)
    id("kotlin-parcelize")
}

// Reading version from file (created in GitHub Actions)
val versionPropsFile = file("../version.properties")
val versionProps = Properties()

if (versionPropsFile.exists()) {
    versionProps.load(FileInputStream(versionPropsFile))
}

// Version functions
fun getVersionName(): String {
    return versionProps.getProperty("VERSION_NAME") 
        ?: System.getenv("VERSION_NAME") 
        ?: "1.0.0-dev"
}

fun getVersionCode(): Int {
    return versionProps.getProperty("VERSION_CODE")?.toIntOrNull() 
        ?: System.getenv("VERSION_CODE")?.toIntOrNull() 
        ?: 1
}

android {
    namespace = "com.example.tmdbai"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tmdbai"
        minSdk = 24
        targetSdk = 34
        versionCode = getVersionCode()
        versionName = getVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        // Compose configuration
        vectorDrawables {
            useSupportLibrary = true
        }

        // Build config fields for debugging
        buildConfigField("String", "BUILD_TIME", "\"${System.currentTimeMillis()}\"")
        buildConfigField("String", "GIT_SHA", "\"${System.getenv("GITHUB_SHA") ?: "unknown"}\"")
        buildConfigField("String", "VERSION_NAME", "\"${getVersionName()}\"")
        buildConfigField("int", "VERSION_CODE", "${getVersionCode()}")
        
        // MCP Server URL configuration
        buildConfigField("String", "MCP_SERVER_URL", "\"https://your-ngrok-url.ngrok.io\"")
        buildConfigField("String", "TMDB_BASE_URL", "\"https://api.themoviedb.org/3/\"")
    }

    // Signing configuration
    signingConfigs {
        create("release") {
            keyAlias = System.getenv("KEY_ALIAS") ?: "tmdbai"
            keyPassword = System.getenv("KEY_PASSWORD") ?: ""
            storeFile = file("tmdbai-release-key.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD") ?: ""
            
            // Enable V1 and V2 signing for compatibility
            enableV1Signing = true
            enableV2Signing = true
            enableV3Signing = true
            enableV4Signing = true
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            
            // Build config for debug
            buildConfigField("boolean", "DEBUG_MODE", "true")
            buildConfigField("String", "BUILD_TYPE", "\"debug\"")
        }
        
        release {
            isMinifyEnabled = true
            isDebuggable = false
            isShrinkResources = true
            
            // ProGuard/R8 settings
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            
            // Build config for release
            buildConfigField("boolean", "DEBUG_MODE", "false")
            buildConfigField("String", "BUILD_TYPE", "\"release\"")
            
            // Packaging settings
            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    excludes += "/META-INF/*.kotlin_module"
                    excludes += "/META-INF/DEPENDENCIES"
                    excludes += "/META-INF/LICENSE*"
                    excludes += "/META-INF/NOTICE*"
                }
            }
        }
    }

    // Flavors for different configurations
    flavorDimensions += "environment"
    productFlavors {
        create("development") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            
            buildConfigField("String", "API_BASE_URL", "\"https://dev-api.example.com/\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "true")
        }
        
        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            
            buildConfigField("String", "API_BASE_URL", "\"https://staging-api.example.com/\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "true")
        }
        
        create("production") {
            dimension = "environment"
            
            buildConfigField("String", "API_BASE_URL", "\"https://api.example.com/\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "false")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    
    kotlinOptions {
        jvmTarget = "11"
        
        // Enable experimental APIs
        freeCompilerArgs += listOf(
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
        )
    }
    
    buildFeatures {
        compose = true
        buildConfig = true
        viewBinding = false
        dataBinding = false
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    
    // Testing configuration
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
        
        animationsDisabled = true
    }
    
    // Lint configuration
    lint {
        abortOnError = false
        checkReleaseBuilds = true
        disable += setOf("MissingTranslation", "ExtraTranslation")
        warningsAsErrors = true
        baseline = file("lint-baseline.xml")
    }
    
    // Bundle configuration for Play Store
    bundle {
        language {
            enableSplit = false
        }
        density {
            enableSplit = true
        }
        abi {
            enableSplit = true
        }
    }
}

dependencies {
    // Jetpack Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    
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
    
    // Navigation Compose
    implementation(libs.androidx.navigation.compose)
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.6")
    
    // ViewModel & State Management
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    
    // Dependency Injection - Koin
    implementation("io.insert-koin:koin-android:3.5.3")
    implementation("io.insert-koin:koin-androidx-compose:3.5.3")
    implementation("io.insert-koin:koin-core:3.5.3")
    
    // Coroutines & Reactive Programming
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    
    // Networking - Ktor for MCP protocol
    implementation("io.ktor:ktor-client-android:2.3.7")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    implementation("io.ktor:ktor-client-logging:2.3.7")
    implementation("io.ktor:ktor-client-auth:2.3.7")
    
    // Image Loading - Coil
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("io.coil-kt:coil-gif:2.5.0")
    implementation("io.coil-kt:coil-svg:2.5.0")
    
    // JSON Processing - Gson for backward compatibility
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    
    // Date/Time
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
    
    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.1")
    
    // SystemUI Controller
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")
    
    // Permissions
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")
    
    // Pull to Refresh
    implementation("androidx.compose.material:material:1.6.1")
    
    // Paging for movie lists
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
    implementation("androidx.paging:paging-compose:3.2.1")
    
    // DataStore for preferences
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    
    // Timber for logging
    implementation("com.jakewharton.timber:timber:5.0.1")
    
    // Testing dependencies
    testImplementation(libs.junit)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("app.cash.turbine:turbine:1.0.0")
    testImplementation("io.insert-koin:koin-test:3.5.3")
    testImplementation("io.insert-koin:koin-test-junit4:3.5.3")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    
    // Android testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.6")
    androidTestImplementation("io.insert-koin:koin-test:3.5.3")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    
    // Debug dependencies
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12")
}

// Tasks for automation
tasks.register("generateVersionInfo") {
    doLast {
        val versionFile = file("src/main/assets/version.json")
        versionFile.parentFile.mkdirs()
        
        val versionInfo = mapOf(
            "versionName" to getVersionName(),
            "versionCode" to getVersionCode(),
            "buildTime" to System.currentTimeMillis(),
            "gitSha" to (System.getenv("GITHUB_SHA") ?: "unknown"),
            "buildType" to (findProperty("buildType") ?: "unknown")
        )
        
        versionFile.writeText(
            groovy.json.JsonBuilder(versionInfo).toPrettyString()
        )
    }
}

// Automatically generate version before build
tasks.whenTaskAdded {
    if (name.contains("assemble") || name.contains("bundle")) {
        dependsOn("generateVersionInfo")
    }
}

// Copy APK to project root for convenience
tasks.register<Copy>("copyReleaseApk") {
    from("build/outputs/apk/production/release")
    into("../")
    include("*.apk")
    rename { "TmdbAi-${getVersionName()}-release.apk" }
}

tasks.whenTaskAdded {
    if (name == "assembleProductionRelease") {
        finalizedBy("copyReleaseApk")
    }
}