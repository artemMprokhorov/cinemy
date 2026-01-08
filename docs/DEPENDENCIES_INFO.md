# Dependencies Information

**Cinemy - Dependencies Information**
**Last Updated**: 2026-01-07
**Version**: 3.0.0

> **📚 Layer Documentation**: For detailed implementation of each layer, see:
> - [🗄️ Data Layer](./app_layers/DATA_LAYER.md) - Data layer dependencies and MCP integration
> - [🤖 ML Layer](./app_layers/ML_LAYER.md) - Adaptive ML runtime dependencies and TensorFlow Lite
> - [🧭 Navigation Layer](./app_layers/NAVIGATION_LAYER.md) - Navigation dependencies
> - [🎨 Presentation Layer](./app_layers/PRESENTATION_LAYER.md) - ViewModel dependencies
> - [🖼️ UI Components Layer](./app_layers/UI_COMPONENTS_LAYER.md) - UI component dependencies
> - [🔧 Utils Layer](./app_layers/UTILS_LAYER.md) - Utility dependencies

## 📦 Dependencies Overview

The project uses a modern technology stack for Android development with a focus on performance, testability, and scalability.

## 🏗️ Core Dependencies

### 📱 Android Core

| Dependency | Version | Purpose | Justification |
|------------|---------|---------|---------------|
| `androidx.core:core-ktx` | 1.15.0 | Basic Android components | Standard library for Kotlin Android |
| `androidx.lifecycle:lifecycle-runtime-ktx` | 2.8.7 | Lifecycle components | Component lifecycle management |
| `androidx.activity:activity-compose` | 1.9.3 | Compose Activity | Compose integration with Activity |

### 🎨 Jetpack Compose

| Dependency | Version | Purpose | Justification |
|------------|---------|---------|---------------|
| `androidx.compose:compose-bom` | 2025.01.00 | Compose BOM | Compose version management |
| `androidx.compose.ui:ui` | BOM | UI components | Core UI elements |
| `androidx.compose.material3:material3` | BOM | Material Design 3 | Modern design system |
| `androidx.compose.ui:ui-tooling-preview` | BOM | Preview tools | UI preview |
| `androidx.compose.ui:ui-tooling` | BOM | Debug tools | Compose UI debugging |

**Selection Justification**: Compose is a modern declarative UI framework that replaces the traditional View system. Material Design 3 provides a modern appearance.

### 🧭 Navigation

| Dependency | Version | Purpose | Justification |
|------------|---------|---------|---------------|
| `androidx.navigation:navigation-compose` | 2.9.3 | Compose navigation | Type-safe navigation between screens |
| `androidx.navigation:navigation-runtime-ktx` | 2.9.3 | Runtime navigation | Basic navigation components |

**Selection Justification**: Navigation Compose provides type-safe navigation with Compose support. Version 2.9.3 is the latest stable version.

### 🔄 State Management

| Dependency | Version | Purpose | Justification |
|------------|---------|---------|---------------|
| `androidx.lifecycle:lifecycle-viewmodel-compose` | 2.8.7 | ViewModel Compose | ViewModel integration with Compose |
| `androidx.lifecycle:lifecycle-runtime-compose` | 2.8.7 | Runtime Compose | Runtime integration |

**Selection Justification**: ViewModel Compose provides proper ViewModel integration with Compose UI, including automatic lifecycle management.

## 🗜️ Dependency Injection

### 🎯 Koin

| Dependency | Version | Purpose | Justification |
|------------|---------|---------|---------------|
| `io.insert-koin:koin-android` | 3.5.6 | Android Koin | DI for Android |
| `io.insert-koin:koin-androidx-compose` | 3.5.6 | Compose Koin | DI for Compose |
| `io.insert-koin:koin-core` | 3.5.6 | Core Koin | Basic DI components |

**Selection Justification**: Koin was chosen for its ease of use, lack of annotations, and good integration with Kotlin. Alternative Hilt requires more setup.

## 🌐 Networking & Data

### 🚀 Ktor Client

| Dependency | Version | Purpose | Justification |
|------------|---------|---------|---------------|
| `io.ktor:ktor-client-core` | 3.1.0 | Core HTTP client | Basic HTTP functionality |
| `io.ktor:ktor-client-android` | 3.1.0 | Android HTTP client | Android-specific functions |
| `io.ktor:ktor-client-content-negotiation` | 3.1.0 | Content negotiation | Automatic serialization |
| `io.ktor:ktor-serialization-kotlinx-json` | 3.1.0 | JSON serialization | JSON processing |
| `io.ktor:ktor-client-logging` | 3.1.0 | HTTP logging | Network request debugging |
| `io.ktor:ktor-client-auth` | 3.1.0 | Authentication | HTTP authentication |

**Selection Justification**: Ktor is a modern HTTP client from JetBrains, written in Kotlin. Provides better integration with Kotlin and coroutines compared to Retrofit.

### 📊 JSON Processing

| Dependency | Version | Purpose | Justification |
|------------|---------|---------|---------------|
| `org.jetbrains.kotlinx:kotlinx-serialization-json` | 1.6.2 | JSON serialization | Main JSON library |
| `com.google.code.gson:gson` | 2.10.1 | Gson (compatibility) | Backward compatibility |

**Selection Justification**: Kotlinx Serialization is a native Kotlin library for serialization. Gson is kept for backward compatibility during migration.

## 🖼️ Image Loading

### 🎨 Coil

| Dependency | Version | Purpose | Justification |
|------------|---------|---------|---------------|
| `io.coil-kt.coil3:coil-compose` | 3.1.0 | Compose integration | Image loading in Compose |
| `io.coil-kt.coil3:coil-gif` | 3.1.0 | GIF support | Animated images |
| `io.coil-kt.coil3:coil-svg` | 3.1.0 | SVG support | Vector images |
| `io.coil-kt.coil3:coil-network-okhttp` | 3.1.0 | Network layer | OkHttp networking |

**Selection Justification**: Coil 3.x is a modern Compose Multiplatform image loading library. Provides 25-40% better performance with reduced memory allocations.

## ⚡ Asynchronous Programming

### 🔄 Coroutines

| Dependency | Version | Purpose | Justification |
|------------|---------|---------|---------------|
| `org.jetbrains.kotlinx:kotlinx-coroutines-android` | 1.9.0 | Android coroutines | Asynchronous programming |
| `org.jetbrains.kotlinx:kotlinx-coroutines-core` | 1.9.0 | Core coroutines | Basic coroutines |

**Selection Justification**: Kotlin Coroutines is the native way of asynchronous programming in Kotlin. Provides better code readability compared to RxJava.

## 📱 UI Enhancements

### 🎨 Accompanist

| Dependency | Version | Purpose | Justification |
|------------|---------|---------|---------------|
| `com.google.accompanist:accompanist-permissions` | 0.37.0 | Permissions | Permission management |

**Note**: `accompanist-systemuicontroller` removed (deprecated). Use native `Activity.enableEdgeToEdge()` instead.

**Selection Justification**: Accompanist provides additional components for Compose that are not yet included in the main framework.

### 🔄 Paging

| Dependency | Version | Purpose | Justification |
|------------|---------|---------|---------------|
| `androidx.paging:paging-runtime-ktx` | 3.3.5 | Paging runtime | Data pagination |
| `androidx.paging:paging-compose` | 3.3.5 | Compose paging | UI for pagination |

**Selection Justification**: Paging 3 provides efficient loading and display of large data lists with Compose support.

## 💾 Data Storage

### 📊 DataStore

| Dependency | Version | Purpose | Justification |
|------------|---------|---------|---------------|
| `androidx.datastore:datastore-preferences` | 1.1.1 | Preferences DataStore | Settings storage |

**Selection Justification**: DataStore is a modern replacement for SharedPreferences, providing type-safe API and coroutine support.

## 🧪 Testing

### 📊 Unit Testing

| Dependency | Version | Purpose | Justification |
|------------|---------|---------|---------------|
| `junit:junit` | 4.13.2 | JUnit 4 | Basic unit testing |
| `org.jetbrains.kotlinx:kotlinx-coroutines-test` | 1.9.0 | Coroutines testing | Coroutine testing |
| `io.mockk:mockk` | 1.13.14 | Mocking | Mock creation |
| `app.cash.turbine:turbine` | 1.2.0 | Flow testing | Flow testing |
| `io.insert-koin:koin-test` | 3.5.6 | Koin testing | DI testing |

**Selection Justification**: MockK is a modern mocking library for Kotlin. Turbine is specifically designed for Flow testing.

### 📱 Android Testing

| Dependency | Version | Purpose | Justification |
|------------|---------|---------|---------------|
| `androidx.test.ext:junit` | 1.2.1 | Android JUnit | JUnit for Android |
| `androidx.test.espresso:espresso-core` | 3.6.1 | Espresso | UI testing |
| `androidx.compose.ui:ui-test-junit4` | BOM | Compose testing | Compose UI testing |

**Selection Justification**: Espresso is the standard library for Android UI testing. Compose UI Test provides testing for Compose components.

## 🔧 Development Tools

### 📝 Logging

| Dependency | Version | Purpose | Justification |
|------------|---------|---------|---------------|
| `com.jakewharton.timber:timber` | 5.0.1 | Logging | Structured logging |

**Selection Justification**: Timber is a popular logging library that provides a convenient API and automatic tag management.

### 🤖 Machine Learning

| Dependency | Version | Purpose | Justification |
|------------|---------|---------|---------------|
| `org.tensorflow:tensorflow-lite` | 2.13.0 | TensorFlow Lite core | Production ML model inference |
| `org.tensorflow:tensorflow-lite-support` | 0.4.4 | TensorFlow Lite support | ML utilities and helpers |

**Selection Justification**: TensorFlow Lite 2.13.0 provides optimized mobile ML inference with hardware acceleration. The support library offers additional utilities for text processing and model management.

**Note**: LiteRT (Google AI Edge) is the new name for TensorFlow Lite runtime. Migration planned for future release.

#### **ML Model Specifications**
- **Model File**: `production_sentiment_full_manual.tflite` (3.8MB)
- **Architecture**: BERT-based transformer for sentiment analysis
- **Input**: 512-token sequences with BERT tokenization
- **Output**: 3-class sentiment classification (negative, neutral, positive)
- **Vocabulary**: 30,522 tokens with special BERT tokens
- **Performance**: NNAPI and XNNPACK acceleration enabled

#### **ML Dependencies Details**
```kotlin
// TensorFlow Lite Core
implementation("org.tensorflow:tensorflow-lite:2.13.0")

// TensorFlow Lite Support Library
implementation("org.tensorflow:tensorflow-lite-support:0.4.4")
```

**Hardware Acceleration**:
- **NNAPI**: Android Neural Networks API for hardware acceleration
- **XNNPACK**: Optimized CPU inference engine
- **Multi-threading**: 4-thread parallel processing

### 🎨 Debug Tools

| Dependency | Version | Purpose | Justification |
|------------|---------|---------|---------------|
| `com.squareup.leakcanary:leakcanary-android` | 2.12 | Memory leak detection | Memory leak debugging |

**Selection Justification**: LeakCanary is a tool for detecting memory leaks in Android applications.

## 📋 Version Management

### 🔄 Version Catalog

The project uses Gradle Version Catalog (`gradle/libs.versions.toml`) for centralized dependency version management.

**Advantages**:
- Centralized version management
- Easy dependency updates
- Version consistency
- Simplified migration

### 📊 Version Compatibility

| Component | Version | Compatibility |
|-----------|---------|---------------|
| AGP | 8.7.3 | Android Studio Ladybug+ |
| Kotlin | 2.1.0 | Stable version |
| Compose Compiler | Integrated | Built into Kotlin 2.0+ |
| Compose BOM | 2025.01.00 | Latest stable version |
| Gradle | 8.11.1 | Required for AGP 8.7.3 |

## 🚀 Build Configuration

### 🔧 Build Variants

```kotlin
flavorDimensions += "environment"
productFlavors {
    create("dummy") {
        dimension = "environment"
        applicationIdSuffix = ".dummy"
        versionNameSuffix = "-dummy"
    }
    create("prod") {
        dimension = "environment"
    }
}
```

### 📱 Build Types

```kotlin
buildTypes {
    debug {
        isMinifyEnabled = false
        isDebuggable = true
    }
    release {
        isMinifyEnabled = true
        isDebuggable = false
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
}
```

## 🔒 Security Considerations

### 🛡️ API Keys

```kotlin
buildConfigField("String", "MCP_SERVER_URL", "\"https://your-ngrok-url.ngrok.io\"")
buildConfigField("String", "TMDB_BASE_URL", "\"https://api.themoviedb.org/3/\"")
```

**Recommendations**:
- Do not commit API keys to repository
- Use environment variables
- Store secrets in a secure location

### 🔐 Signing Configuration

```kotlin
signingConfigs {
    create("release") {
        keyAlias = System.getenv("KEY_ALIAS") ?: "tmdbai"
        keyPassword = System.getenv("KEY_PASSWORD") ?: ""
        storeFile = file("tmdbai-release-key.jks")
        storePassword = System.getenv("KEYSTORE_PASSWORD") ?: ""
    }
}
```

## 📈 Performance Optimization

### ⚡ Compose Optimizations

```kotlin
// Compose Compiler is now integrated into Kotlin 2.0+
// No separate composeOptions needed

plugins {
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0"
}
```

### 🧹 ProGuard Configuration

```kotlin
release {
    isMinifyEnabled = true
    isShrinkResources = true
    proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )
}
```

## 🔄 Dependency Updates

### 📅 Update Schedule

- **Weekly**: Security update checks
- **Monthly**: Minor version updates
- **Quarterly**: Major version updates

### 🚨 Breaking Changes

When updating dependencies:
1. Check changelog
2. Test on dev branch
3. Update documentation
4. Conduct code review

## 📚 Alternative Libraries

### 🔄 Considered Alternatives

| Current | Alternative | Selection Reason |
|---------|-------------|------------------|
| Koin | Hilt | Setup simplicity |
| Ktor | Retrofit | Kotlin-first approach |
| Coil | Glide | Modernity and performance |
| StateFlow | LiveData | Kotlin-first approach |
| TensorFlow Lite | ML Kit | Local model control |

