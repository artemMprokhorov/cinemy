# Development Guide

**Cinemy - Development Guide**  
**Last Updated**: 2025-01-27  
**Version**: 3.0.0

> **📚 Layer Documentation**: For detailed implementation of each layer, see:
> - [🗄️ Data Layer](./DATA_LAYER.md) - Data layer architecture and implementation
> - [🤖 ML Layer](./ML_LAYER.md) - Adaptive ML runtime with LiteRT integration
> - [🧭 Navigation Layer](./NAVIGATION_LAYER.md) - Navigation and routing system
> - [🎨 Presentation Layer](./PRESENTATION_LAYER.md) - ViewModels and state management
> - [🖼️ UI Components Layer](./UI_COMPONENTS_LAYER.md) - UI components and theming
> - [🔧 Utils Layer](./UTILS_LAYER.md) - Utility classes and helper functions

## 🆕 Latest Updates (v2.10.0)

### 🤖 **Adaptive ML Runtime with LiteRT Integration** - January 2025
- **Hardware Detection**: Automatic detection of GPU, NNAPI, XNNPACK, and LiteRT support
- **LiteRT Integration**: Full implementation of Android's official ML inference runtime
- **Model Consistency**: LiteRT uses the same local BERT model as TensorFlow Lite
- **Hardware Acceleration**: Automatic selection of optimal ML runtime based on device capabilities
- **Performance Optimization**: GPU acceleration (~50ms), NPU acceleration (~80ms), CPU fallback (~200ms)

### 🧠 **ML Architecture Enhancements**
- **AdaptiveMLRuntime.kt**: Intelligent runtime selection and management
- **LiteRTSentimentModel.kt**: Full LiteRT implementation with hardware acceleration
- **HardwareDetection.kt**: Comprehensive hardware capability detection
- **SentimentAnalyzer.kt**: Updated with four-tier fallback system

### 🔧 **Technical Implementation**
- **Google Play Services ML Kit**: LiteRT detection through Play Services version checking
- **Hardware Delegates**: GPU, NNAPI, XNNPACK acceleration support
- **Model Sharing**: LiteRT and TensorFlow Lite use identical local BERT model
- **Error Handling**: Comprehensive fallback mechanisms for production reliability
- **Documentation**: All code and documentation in English

## 🚀 Development Environment Setup

### 📋 Prerequisites

- **Android Studio**: Hedgehog (2023.1.1) or newer
- **JDK**: 17 or newer
- **Android SDK**: API 36 (Android 14)
- **Gradle**: 8.10.2
- **Kotlin**: 1.9.22

### ⚙️ Project Setup

1. **Clone Repository**
   ```bash
   git clone https://github.com/artemMprokhorov/cinemy.git
   cd cinemy
   ```

2. **Environment Variables Setup**
   ```bash
   # Create local.properties
   MCP_SERVER_URL=https://your-ngrok-url.ngrok.io
   TMDB_BASE_URL=https://api.themoviedb.org/3/
   TMDB_API_KEY=your_tmdb_api_key_here
   ```

3. **Gradle Sync**
   ```bash
   ./gradlew --refresh-dependencies
   ```

4. **Build Verification**
   ```bash
   # Dummy version (mock data)
   ./gradlew assembleDummyDebug
   
   # Production version (real backend)
   ./gradlew assembleProdDebug
   ```

### 🔧 Android Studio Configuration

1. **Kotlin Plugin**: Ensure Kotlin plugin is installed
2. **Compose Preview**: Enable Compose Preview in settings
3. **Code Style**: Configure Kotlin code style
4. **Live Templates**: Set up live templates for MVI

### 🏗️ Build Variants

The project supports three build variants:

| Variant | Purpose | Data Source | Package ID |
|---------|---------|-------------|------------|
| **dummyDebug** | Development | Mock data only | `org.studioapp.cinemy.dummy.debug` |
| **prodDebug** | Testing | Real backend + fallback | `org.studioapp.cinemy.debug` |
| **prodRelease** | Production | Real backend only | `org.studioapp.cinemy` |

#### Installation and Launch

```bash
# Dummy version (mock data)
./gradlew installDummyDebug
adb shell am start -n org.studioapp.cinemy.dummy.debug/org.studioapp.cinemy.MainActivity

# Production version (real backend)
./gradlew installProdDebug
adb shell am start -n org.studioapp.cinemy.debug/org.studioapp.cinemy.MainActivity
```

## 🎨 Edge-to-Edge Display

### 📱 Edge-to-Edge Setup

The project supports full-screen mode (edge-to-edge) on all Android 5.0+ versions:

#### 1. **VersionUtils Configuration**
```kotlin
// VersionUtils.kt
object Versions {
    const val ANDROID_5 = Build.VERSION_CODES.LOLLIPOP // API 21 - Minimum for edge-to-edge
}

fun safeEnableEdgeToEdge(activity: ComponentActivity) {
    if (Build.VERSION.SDK_INT >= Versions.ANDROID_5) {
        activity.enableEdgeToEdge()
    }
}
```

#### 2. **MainActivity Setup**
```kotlin
// MainActivity.kt
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    // Enable edge-to-edge on supported versions
    VersionUtils.safeEnableEdgeToEdge(this)
    
    setContent {
        // Your UI content
    }
}
```

#### 3. **UI Screen Implementation**
```kotlin
// All main screens should use systemBarsPadding()
Box(
    modifier = Modifier
        .fillMaxSize()
        .background(SplashBackground)
        .systemBarsPadding() // ← This is crucial for proper edge-to-edge
) {
    // Your content
}
```

### ✅ **Best Practices**

1. **Always use `systemBarsPadding()`** on main containers
2. **Test on different Android versions** (API 21+)
3. **Verify content doesn't overlap** with system bars
4. **Use `VersionUtils.safeEnableEdgeToEdge()`** for compatibility

## 📱 Foldable Device Development

### 🎯 **Device Type Detection**

#### **DeviceUtils.kt Usage**
```kotlin
// In Composable functions
@Composable
fun MyScreen() {
    val deviceType = getDeviceType()
    val isFoldable = isFoldableDevice()
    val supportsDual = supportsDualPane()
    
    when (deviceType) {
        DeviceUtils.DeviceType.FOLDABLE -> {
            // Foldable-specific layout
        }
        DeviceUtils.DeviceType.TABLET -> {
            // Tablet layout
        }
        DeviceUtils.DeviceType.PHONE -> {
            // Phone layout
        }
    }
}
```

#### **Adaptive Layout Implementation**
```kotlin
@Composable
fun MoviesScreen() {
    AdaptiveLayout(
        leftPane = { MoviesList() },
        rightPane = { MovieDetails() }
    )
}
```

### 🔄 **Configuration Change Handling**

#### **MainActivity Setup**
```kotlin
class MainActivity : ComponentActivity() {
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        
        // Handle device state changes
        val newDeviceType = DeviceUtils.getDeviceType(this)
        handleDeviceTypeChange(newDeviceType)
    }
}
```

### 🪟 **WindowInsets Management**

#### **Foldable-Specific Insets**
```kotlin
@Composable
fun MyScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .adaptiveInsetsPadding() // Automatically chooses appropriate insets
    ) {
        // Content
    }
}
```

## 🔧 Code Quality & Best Practices

### 📝 **String Resources & Internationalization**

#### ✅ **Required Patterns**
```kotlin
// ❌ BAD - Hardcoded strings
Text("Sentiment Analysis Reviews")
Text("Positive Reviews (${count})")

// ✅ GOOD - String resources
Text(stringResource(R.string.sentiment_analysis_title))
Text(stringResource(R.string.sentiment_positive_reviews, count))
```

### 🎯 **Constants Organization**

#### ✅ **Required Patterns**
```kotlin
// ❌ BAD - Hardcoded values
val confidence = 0.85
val timeout = 30000L
val errorMessage = "Analysis failed"

// ✅ GOOD - Constants in companion object
companion object {
    const val DEFAULT_CONFIDENCE = 0.85
    const val HTTP_REQUEST_TIMEOUT_MS = 30000L
    const val ERROR_ANALYSIS_FAILED = "Analysis failed"
}
```

### 🚀 **Error Handling with runCatching**

#### ✅ **Required Patterns**
```kotlin
// ❌ BAD - try/catch blocks
try {
    val result = performAnalysis()
    return result
} catch (e: Exception) {
    Log.e("Error", "Analysis failed", e)
    return null
}

// ✅ GOOD - runCatching approach
return runCatching {
    performAnalysis()
}.onFailure { e ->
    if (BuildConfig.DEBUG) {
        Log.e("Analysis", "Analysis failed", e)
    }
}.getOrNull()
```

### 📊 **Debug-Only Logging**

#### ✅ **Required Patterns**
```kotlin
// ❌ BAD - Always logging
Log.d("Tag", "Debug message")
Log.e("Tag", "Error message", exception)

// ✅ GOOD - Debug-only logging
if (BuildConfig.DEBUG) {
    Log.d("Tag", "Debug message")
    Log.e("Tag", "Error message", exception)
}
```

### 🎨 **UI Component Patterns**

#### ✅ **Required Patterns**
```kotlin
// ❌ BAD - Hardcoded values in UI
Text(
    text = "Positive Reviews (${reviews.size})",
    color = Color(0xFF4CAF50),
    fontSize = 24.sp
)

// ✅ GOOD - String resources and constants
Text(
    text = stringResource(R.string.sentiment_positive_reviews, reviews.size),
    color = SentimentPositive,
    fontSize = Typography24
)
```

### 🧪 **ML Component Patterns**

#### ✅ **Required Patterns**
```kotlin
// ❌ BAD - Hardcoded ML configuration
class SentimentAnalyzer {
    private val confidence = 0.85
    private val errorMessage = "Analysis failed"
    
    fun analyze(text: String): SentimentResult {
        try {
            // Analysis logic
        } catch (e: Exception) {
            return SentimentResult.error("Analysis failed")
        }
    }
}

// ✅ GOOD - Constants and runCatching
class SentimentAnalyzer {
    companion object {
        const val DEFAULT_CONFIDENCE = 0.85
        const val ERROR_ANALYSIS_FAILED = "Analysis failed"
    }
    
    fun analyze(text: String): SentimentResult {
        return runCatching {
            // Analysis logic
        }.getOrElse { e ->
            if (BuildConfig.DEBUG) {
                Log.e("SentimentAnalyzer", ERROR_ANALYSIS_FAILED, e)
            }
            SentimentResult.error(ERROR_ANALYSIS_FAILED)
        }
    }
}
```

### 📦 **Import Management**

#### ✅ **Required Patterns**
```kotlin
// ❌ BAD - Wildcard imports
import org.junit.Assert.*
import io.mockk.*
import org.studioapp.cinemy.data.model.*

// ✅ GOOD - Explicit imports
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.studioapp.cinemy.data.model.ButtonConfiguration
import org.studioapp.cinemy.data.model.ColorScheme
import org.studioapp.cinemy.data.model.GeminiColors
import org.studioapp.cinemy.data.model.Genre
import org.studioapp.cinemy.data.model.Meta
import org.studioapp.cinemy.data.model.Movie
import org.studioapp.cinemy.data.model.MovieDetails
import org.studioapp.cinemy.data.model.MovieListData
import org.studioapp.cinemy.data.model.MovieListResponse
import org.studioapp.cinemy.data.model.Pagination
import org.studioapp.cinemy.data.model.ProductionCompany
import org.studioapp.cinemy.data.model.Result
import org.studioapp.cinemy.data.model.SearchInfo
import org.studioapp.cinemy.data.model.SentimentReviews
import org.studioapp.cinemy.data.model.StringConstants
import org.studioapp.cinemy.data.model.TextConfiguration
import org.studioapp.cinemy.data.model.UiConfiguration
```

## 📏 Code Development Rules

### 🏗️ Architectural Principles

#### ✅ Mandatory Rules

1. **Follow MVI Pattern**
   - Each screen has its own ViewModel
   - ViewModel processes Intents
   - UI reacts to State changes

2. **Use Clean Architecture**
   - Separate code into layers
   - Dependencies point inward
   - External layers don't know about internal ones

3. **Apply Dependency Injection**
   - Use Koin for DI
   - Don't create objects directly
   - Use interfaces for abstraction

4. **Use Constants Instead of Hardcoded Values**
   - All strings, numbers, and boolean values should be in `StringConstants.kt`
   - Use constants in `@SerializedName` annotations
   - Don't use constants for log messages (only inline strings)

5. **UI Layer Constants Rules**
   - **Dimensions**: All sizes (16.dp, 8.dp, etc.) → `Dimens.kt`
   - **Float Values**: All float values (0.1f, 0.7f, 2.0f, etc.) → `Floats.kt`
   - **UI Text**: All user strings → `strings.xml`
   - **Image URLs**: All image URLs → `ImageConfig.kt`
   - **UI Constants**: Numeric constants (thresholds, limits) → `UIConstants.kt`
   - **No Hardcoded Values**: No hardcoded values in UI components
   - **Theme Cleanup**: Regularly remove unused resources from theme files
   - **Resource Optimization**: Maintain minimal set of necessary constants

#### 🔤 Naming Conventions

```kotlin
// ✅ Correct
class MoviesListViewModel : ViewModel()
sealed class MoviesListIntent
data class MoviesListState
interface MovieRepository

// ❌ Incorrect
class MoviesListVM : ViewModel()
class MoviesListActions
class MoviesListData
class MovieRepo
```

#### 📁 File Structure

```
presentation/movieslist/
├── MoviesListViewModel.kt      # ViewModel
├── MoviesListIntent.kt         # Intent classes
└── MoviesListState.kt          # State classes

ui/movieslist/
└── MoviesListScreen.kt         # UI screen

data/repository/
├── MovieRepository.kt           # Interface
└── MovieRepositoryImpl.kt      # Implementation

data/model/
└── StringConstants.kt          # All project constants
```

## 🧪 Testing Guidelines

### 📊 Unit Testing

#### ViewModel Testing

```kotlin
// test/presentation/movieslist/MoviesListViewModelTest.kt
@RunWith(MockKJUnitRunner::class)
class MoviesListViewModelTest {
    
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    
    private lateinit var viewModel: MoviesListViewModel
    private lateinit var mockRepository: MovieRepository
    
    @Before
    fun setup() {
        mockRepository = mockk()
        viewModel = MoviesListViewModel(mockRepository)
    }
    
    @Test
    fun `when LoadPopularMovies intent is processed, should load movies`() = runTest {
        // Given
        val movies = listOf(Movie(id = 1, title = "Test Movie"))
        val response = MovieListResponse(movies = movies, page = 1, totalPages = 1)
        coEvery { mockRepository.getPopularMovies(1) } returns Result.Success(response)
        
        // When
        viewModel.processIntent(MoviesListIntent.LoadPopularMovies)
        
        // Then
        val state = viewModel.state.first()
        assertThat(state.movies).isEqualTo(movies)
        assertThat(state.isLoading).isFalse()
        assertThat(state.error).isNull()
    }
}
```

#### ML Component Testing

```kotlin
// test/ml/AdaptiveMLRuntimeTest.kt
@RunWith(MockKJUnitRunner::class)
class AdaptiveMLRuntimeTest {
    
    private lateinit var adaptiveRuntime: AdaptiveMLRuntime
    private lateinit var mockContext: Context
    private lateinit var mockHardwareDetection: HardwareDetection
    
    @Before
    fun setup() {
        mockContext = mockk()
        mockHardwareDetection = mockk()
        adaptiveRuntime = AdaptiveMLRuntime.getInstance(mockContext)
    }
    
    @Test
    fun `should select LiteRT when GPU and Play Services available`() = runTest {
        // Given
        every { mockHardwareDetection.detectGpuSupport() } returns true
        every { mockHardwareDetection.detectLiteRTSupport() } returns true
        
        // When
        val runtime = adaptiveRuntime.selectOptimalRuntime()
        
        // Then
        assertThat(runtime).isEqualTo(MLRuntime.LITERT_GPU)
    }
}
```

### 🔗 Integration Tests

```kotlin
// test/integration/MLIntegrationTest.kt
@RunWith(AndroidJUnit4::class)
class MLIntegrationTest {
    
    @Test
    fun `adaptive runtime should fallback gracefully on hardware failure`() = runTest {
        // Given
        val adaptiveRuntime = AdaptiveMLRuntime.getInstance(context)
        val hardwareDetection = HardwareDetection.getInstance(context)
        
        // When
        adaptiveRuntime.initialize()
        val result = adaptiveRuntime.analyzeSentiment("This movie is amazing!")
        
        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.sentiment).isIn(SentimentType.values().toList())
    }
}
```

### 📱 UI Tests

```kotlin
// androidTest/ui/movieslist/MoviesListScreenTest.kt
@RunWith(AndroidJUnit4::class)
class MoviesListScreenUITest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun clickOnMovie_navigatesToMovieDetail() {
        // Given
        val movies = listOf(Movie(id = 1, title = "Test Movie"))
        val state = MoviesListState(movies = movies, isLoading = false)
        var clickedMovieId: Int? = null
        
        // When
        composeTestRule.setContent {
            CinemyTheme {
                MoviesListScreen(
                    onMovieClick = { movieId -> clickedMovieId = movieId },
                    onBackPressed = {},
                    viewModel = FakeMoviesListViewModel(state)
                )
            }
        }
        
        // Then
        composeTestRule.onNodeWithText("Test Movie").performClick()
        assertThat(clickedMovieId).isEqualTo(1)
    }
}
```

## 🐛 Troubleshooting Common Issues

### ❌ Build Errors

#### Compose Compiler Issues

```bash
# Clean and rebuild
./gradlew clean
./gradlew assembleDummyDebug
```

#### Dependency Conflicts

```bash
# View dependency tree
./gradlew app:dependencies

# Force version resolution
configurations.all {
    resolutionStrategy.force 'androidx.core:core-ktx:1.12.0'
}
```

### ❌ Runtime Errors

#### Navigation Issues

```kotlin
// ✅ Correct - argument validation
composable(
    route = Screen.MovieDetail(0).route,
    arguments = listOf(
        navArgument("movieId") {
            type = NavType.IntType
            defaultValue = 1 // Add defaultValue
        }
    )
) { backStackEntry ->
    val movieId = backStackEntry.arguments?.getInt("movieId") ?: 1
    MovieDetailScreen(movieId = movieId)
}
```

#### ML Runtime Issues

```kotlin
// ✅ Correct - hardware detection
val hardwareDetection = HardwareDetection.getInstance(context)
val capabilities = hardwareDetection.detectHardwareCapabilities()

if (capabilities.hasGpu && capabilities.hasLiteRT) {
    // Use LiteRT with GPU acceleration
    val runtime = MLRuntime.LITERT_GPU
} else if (capabilities.hasGpu) {
    // Use TensorFlow Lite with GPU
    val runtime = MLRuntime.TENSORFLOW_LITE_GPU
} else {
    // Use CPU fallback
    val runtime = MLRuntime.TENSORFLOW_LITE_CPU
}
```

### 🔧 Performance Issues

#### Memory Leaks

```kotlin
// ✅ Correct - using viewModelScope
class MoviesListViewModel : ViewModel() {
    fun loadMovies() {
        viewModelScope.launch {
            // Coroutine automatically cancelled when ViewModel is destroyed
            val result = repository.getMovies()
            _state.value = _state.value.copy(movies = result)
        }
    }
}

// ❌ Incorrect - using GlobalScope
fun loadMovies() {
    GlobalScope.launch {
        // May cause memory leaks
    }
}
```

#### ML Performance

```kotlin
// ✅ Correct - adaptive runtime selection
val adaptiveRuntime = AdaptiveMLRuntime.getInstance(context)
adaptiveRuntime.initialize()

// Get optimal runtime for current device
val runtimeInfo = adaptiveRuntime.getRuntimeInfo()
if (runtimeInfo.performanceScore < 50) {
    // Consider using keyword fallback for better performance
    val result = adaptiveRuntime.analyzeSentiment(text)
}
```

## 📚 Useful Resources

### 🔗 Documentation

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
- [Koin](https://insert-koin.io/)
- [Ktor](https://ktor.io/)
- [Material Design 3](https://m3.material.io/)
- [TensorFlow Lite](https://www.tensorflow.org/lite)
- [Android ML Kit](https://developers.google.com/ml-kit)

### 🛠️ Tools

- [Android Studio](https://developer.android.com/studio)
- [Layout Inspector](https://developer.android.com/studio/debug/layout-inspector)
- [Profiler](https://developer.android.com/studio/profile)
- [Lint](https://developer.android.com/studio/write/lint)

### 📱 Code Examples

- [Android Architecture Samples](https://github.com/android/architecture-samples)
- [Jetpack Compose Samples](https://github.com/android/compose-samples)
- [Koin Samples](https://github.com/InsertKoinIO/koin-samples)
- [TensorFlow Lite Examples](https://github.com/tensorflow/examples)

---

**Last Updated**: 2025-01-27  
**Document Version**: 3.0.0  
**Status**: Current