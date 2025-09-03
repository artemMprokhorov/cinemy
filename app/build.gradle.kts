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
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.tmdbai"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
        
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
    
    // Enable Kotlin 1.9
    kotlin {
        sourceSets.all {
            languageSettings {
                languageVersion = "1.9"
            }
        }
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
    implementation("io.ktor:ktor-client-android:2.2.0")
    implementation("io.ktor:ktor-client-content-negotiation:2.2.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.0")
    implementation("io.ktor:ktor-client-logging:2.2.0")
    implementation("io.ktor:ktor-client-auth:2.2.0")
    
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