# Architecture Guide

**Cinemy - Architecture Guide**  
**Last Updated**: 2025-01-27  
**Version**: 3.0.0

> **📚 Layer-Specific Documentation**: For detailed implementation of each layer, see:
> - [🗄️ Data Layer](./DATA_LAYER.md) - Data layer architecture and implementation
> - [🤖 ML Layer](./ML_LAYER.md) - Adaptive ML runtime with LiteRT integration
> - [🧭 Navigation Layer](./NAVIGATION_LAYER.md) - Navigation and routing system
> - [🎨 Presentation Layer](./PRESENTATION_LAYER.md) - ViewModels and state management
> - [🖼️ UI Components Layer](./UI_COMPONENTS_LAYER.md) - UI components and theming
> - [🔧 Utils Layer](./UTILS_LAYER.md) - Utility classes and helper functions

## 🏗️ Architecture Overview

Cinemy is built on **Clean Architecture** principles using the **MVI (Model-View-Intent)** pattern. The architecture ensures clear separation of concerns, testability, and scalability.

## 🎯 MVI (Model-View-Intent) Pattern

### 📖 Core Principles

MVI is an architectural pattern that ensures **unidirectional data flow** and **predictable application state**.

```
┌─────────────┐    Intent    ┌─────────────┐    State    ┌─────────────┐
│    View     │ ──────────→ │ ViewModel   │ ──────────→ │    View     │
│             │              │             │             │             │
└─────────────┘              └─────────────┘             └─────────────┘
       │                            │                           │
       │                            ▼                           │
       │                    ┌─────────────┐                     │
       │                    │ Repository  │                     │
       │                    │             │                     │
       │                    └─────────────┘                     │
       │                            │                           │
       └────────────────────────────┼───────────────────────────┘
                                    │
                                    ▼
                            ┌─────────────┐
                            │   MCP       │
                            │  Client     │
                            └─────────────┘
```

### 🔄 Data Flow

1. **User Action** → User performs an action
2. **Intent** → Intent object is created
3. **ViewModel.processIntent()** → Intent processing
4. **Repository Call** → Repository call
5. **MCP Client** → AI server interaction
6. **State Update** → State update
7. **UI Update** → Automatic UI update

## 🏛️ Clean Architecture Layers

### 📱 Presentation Layer (UI + ViewModels)

**Purpose**: Managing UI state and user interactions

**Components**:
- `MoviesListViewModel` - Movie list management
- `MovieDetailViewModel` - Movie details management
- `MoviesListIntent` - Intents for movie list
- `MovieDetailIntent` - Intents for movie details
- `MoviesListState` - Movie list state
- `MovieDetailState` - Movie details state

**Principles**:
- ViewModels don't know about UI details
- State is represented as immutable data class
- Intents describe all possible user actions

### 🎯 Domain Layer (Use Cases + Models)

**Purpose**: Business logic and domain models

**Components**:
- `MovieRepository` - Repository interface
- Domain Models - Business models
- Use Cases - Business operations

**Principles**:
- Independence from external layers
- Clean business logic
- Testability

### 💾 Data Layer (Repository + Data Sources)

**Purpose**: Data management and external APIs

**Components**:
- `MovieRepositoryImpl` - Repository implementation
- `McpClient` - MCP client for AI integration
- `McpHttpClient` - HTTP client
- Data Models - DTO models
- Mappers - Data transformation

**Principles**:
- Repository pattern
- Data source abstraction
- Caching and synchronization

### 🤖 ML Layer (Machine Learning)

**Purpose**: AI-powered sentiment analysis and machine learning capabilities

**Architecture**:
- Adaptive ML runtime system
- LiteRT integration with hardware acceleration
- TensorFlow Lite integration
- Modular data class organization
- Memory leak prevention

**Components**:
- `SentimentAnalyzer` - Main hybrid sentiment analyzer
- `AdaptiveMLRuntime` - Intelligent runtime selection
- `LiteRTSentimentModel` - LiteRT implementation
- `HardwareDetection` - Hardware capability detection
- `TensorFlowSentimentModel` - TensorFlow Lite model
- `model/` - Data classes and model definitions

**Key Features**:
- **Adaptive Runtime**: Intelligent ML runtime selection
- **Hardware Acceleration**: GPU, NPU, NNAPI support
- **Model Consistency**: Same BERT model across runtimes
- **Performance**: Optimized for mobile devices
- **Fallback System**: Comprehensive error handling

## 📁 Folder Structure

```
app/src/main/java/org/studioapp/cinemy/
├── Cinemy.kt                    # Main application class
├── navigation/                   # Navigation
│   ├── Navigation.kt            # Main navigation
│   └── Screen.kt                # Screen definitions
├── ui/                          # UI Layer
│   ├── components/              # Reusable components
│   ├── movieslist/              # Movie list screen
│   ├── moviedetail/             # Movie details screen
│   ├── splash/                  # Splash screen
│   └── theme/                   # Theme and styles
├── presentation/                 # Presentation Layer
│   ├── di/                      # DI modules
│   ├── commons/                 # Common components
│   ├── movieslist/              # MoviesList ViewModel
│   └── moviedetail/             # MovieDetail ViewModel
├── data/                        # Data Layer
│   ├── di/                      # Data DI modules
│   ├── mcp/                     # MCP client and models
│   ├── model/                   # Data models
│   ├── mapper/                  # Data mappers
│   ├── remote/                  # Remote data sources
│   └── repository/              # Repositories
├── ml/                          # ML Layer
│   ├── model/                   # Data classes and models
│   │   ├── SentimentResult.kt   # Sentiment analysis result
│   │   ├── ModelInfo.kt         # Model information
│   │   ├── KeywordSentimentModel.kt # Keyword-based model
│   │   ├── AlgorithmConfig.kt   # Algorithm configuration
│   │   ├── ContextBoosters.kt   # Context boosters
│   │   ├── EnhancedModelData.kt # Enhanced model data
│   │   ├── ProductionModelData.kt # Production model data
│   │   └── TensorFlowConfig.kt  # TensorFlow configuration
│   ├── SentimentAnalyzer.kt     # Main hybrid analyzer
│   ├── AdaptiveMLRuntime.kt     # Adaptive runtime selector
│   ├── LiteRTSentimentModel.kt   # LiteRT implementation
│   ├── HardwareDetection.kt     # Hardware detection
│   ├── TensorFlowSentimentModel.kt # TensorFlow Lite model
│   └── SimpleKeywordModelFactory.kt # Simple model factory
└── utils/                       # Utilities
```

## 🔄 Dependency Injection (Koin)

### 📦 Modules

#### Presentation Module
```kotlin
val presentationModule = module {
    // ViewModels
    viewModel { MoviesListViewModel(get()) }
    viewModel { MovieDetailViewModel(get()) }
}
```

#### Data Module
```kotlin
val dataModule = module {
    // MCP Client
    single<McpClient> { McpClient() }
    
    // Repository
    single<MovieRepository> { MovieRepositoryImpl(get()) }
}
```

#### ML Module
```kotlin
val mlModule = module {
    // ML Components
    single { SentimentAnalyzer.getInstance(get()) }
    single { AdaptiveMLRuntime.getInstance(get()) }
    single { HardwareDetection.getInstance(get()) }
}
```

### 🔗 Dependency Graph

```
MainActivity
    ↓
AppNavigation
    ↓
Screens → ViewModels → Repository → MCP Client
    ↓
ML Components → Adaptive Runtime → Hardware Detection
    ↓
Koin Container
```

## 🧭 Navigation

### 📱 Navigation Structure

```kotlin
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            MovieAppSplashScreen(
                onSplashComplete = {
                    navController.navigate(Screen.MoviesList.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.MoviesList.route) {
            MoviesListScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Screen.MovieDetail(movieId).createRoute())
                }
            )
        }

        composable(
            route = Screen.MovieDetail(0).route,
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 1
            MovieDetailScreen(movieId = movieId)
        }
    }
}
```

### 🎯 Type-Safe Routes

```kotlin
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object MoviesList : Screen("movies_list")
    data class MovieDetail(val movieId: Int) : Screen("movie_detail/{movieId}") {
        fun createRoute() = "movie_detail/$movieId"
    }
}
```

## 🔄 State Management

### 📊 State Classes

#### MoviesListState
```kotlin
data class MoviesListState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val hasMorePages: Boolean = true
)
```

#### MovieDetailState
```kotlin
data class MovieDetailState(
    val movie: Movie? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
```

### 🔄 StateFlow

```kotlin
class MoviesListViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(MoviesListState())
    val state: StateFlow<MoviesListState> = _state.asStateFlow()
    
    fun processIntent(intent: MoviesListIntent) {
        when (intent) {
            is MoviesListIntent.LoadPopularMovies -> loadPopularMovies()
            is MoviesListIntent.LoadMoreMovies -> loadMoreMovies()
            is MoviesListIntent.Retry -> retry()
        }
    }
}
```

## 🎭 Intent Pattern

### 📝 Intent Classes

#### MoviesListIntent
```kotlin
sealed class MoviesListIntent {
    object LoadPopularMovies : MoviesListIntent()
    object LoadMoreMovies : MoviesListIntent()
    object Retry : MoviesListIntent()
    object Refresh : MoviesListIntent()
    data class SearchMovies(val query: String) : MoviesListIntent()
}
```

#### MovieDetailIntent
```kotlin
sealed class MovieDetailIntent {
    object LoadMovieDetails : MovieDetailIntent()
    object Retry : MovieDetailIntent()
}
```

### 🔄 Intent Processing

```kotlin
fun processIntent(intent: MoviesListIntent) {
    when (intent) {
        is MoviesListIntent.LoadPopularMovies -> {
            _state.value = _state.value.copy(isLoading = true)
            loadPopularMovies()
        }
        is MoviesListIntent.LoadMoreMovies -> {
            loadMoreMovies()
        }
        is MoviesListIntent.Retry -> {
            retry()
        }
        is MoviesListIntent.Refresh -> {
            refresh()
        }
        is MoviesListIntent.SearchMovies -> {
            searchMovies(intent.query)
        }
    }
}
```

## 🌐 MCP Integration

### 🤖 Model Context Protocol

MCP (Model Context Protocol) is a protocol for interacting with AI models.

#### MCP Client with Dynamic Color Support
```kotlin
class McpClient {
    suspend fun getMovieDetailsViaMcp(movieId: Int): Result<MovieDetailsResponse> {
        return runCatching {
            val response = mcpHttpClient.sendRequest<Any>(request)
            
            // Extract dynamic uiConfig from backend response
            val backendUiConfig = data?.get("uiConfig") as? Map<String, Any>
            val uiConfig = if (backendUiConfig != null) {
                // Parse dynamic colors from backend
                UiConfigurationDto(
                    colors = ColorSchemeDto(
                        primary = colors["primary"] as? String ?: "#DC3528",
                        secondary = colors["secondary"] as? String ?: "#E64539",
                        background = colors["background"] as? String ?: "#121212",
                        surface = colors["surface"] as? String ?: "#1E1E1E",
                        // ... other color mappings
                    )
                )
            } else {
                assetDataLoader.loadUiConfig() // Fallback to static assets
            }
            
            Result.Success(
                data = MovieDetailsResponse(...),
                uiConfig = MovieMapper.mapUiConfigurationDtoToUiConfiguration(uiConfig)
            )
        }
    }
}
```

## 🤖 Adaptive ML Runtime Architecture

### 🧠 ML Model Integration

Cinemy features a sophisticated **adaptive ML runtime system** that automatically selects the optimal ML runtime based on device hardware capabilities.

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Text Input    │───▶│  SentimentAnalyzer │───▶│  SentimentResult │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                │
                                ▼
                    ┌─────────────────────┐
                    │  Adaptive Runtime   │
                    │  Selection Logic    │
                    └─────────────────────┘
                                │
                    ┌───────────┴───────────┐
                    ▼                       ▼
        ┌─────────────────────┐    ┌─────────────────────┐
        │ LiteRT Runtime      │    │ TensorFlow Runtime  │
        │ (Hardware Accel)    │    │ (Fallback)         │
        │                     │    │                     │
        │ • GPU: ~50ms        │    │ • GPU: ~70ms        │
        │ • NPU: ~80ms        │    │ • NNAPI: ~100ms     │
        │ • Same BERT Model   │    │ • CPU: ~200ms       │
        └─────────────────────┘    └─────────────────────┘
```

### 🔄 ML Data Flow

1. **Text Input** → Raw text from movie reviews
2. **Hardware Detection** → Detect device capabilities
3. **Runtime Selection** → Choose optimal ML runtime
4. **Text Preprocessing** → BERT tokenization or keyword extraction
5. **Model Inference** → Run sentiment analysis
6. **Result Processing** → Apply confidence thresholds
7. **Fallback Logic** → Switch to alternative runtime if needed
8. **Sentiment Result** → Return structured sentiment data

### 🏗️ ML Components

#### **AdaptiveMLRuntime**
- **Purpose**: Intelligent ML runtime selection and management
- **Hardware Detection**: GPU, NNAPI, XNNPACK, LiteRT support
- **Performance Optimization**: Device-specific runtime selection
- **Fallback System**: Comprehensive error handling

#### **LiteRTSentimentModel**
- **Purpose**: Android's official ML inference runtime
- **Hardware Acceleration**: GPU, NPU, NNAPI optimization
- **Model Consistency**: Same BERT model as TensorFlow Lite
- **Performance**: Optimized for supported devices

#### **HardwareDetection**
- **Purpose**: Comprehensive hardware capability detection
- **GPU Support**: OpenGL ES and Vulkan detection
- **NNAPI Support**: Neural Networks API availability
- **LiteRT Support**: Google Play Services ML Kit detection
- **Performance Scoring**: 0-100 performance score calculation

### 📊 Model Specifications

#### **BERT Production Model**
```kotlin
// Model Configuration
MODEL_FILE = "production_sentiment_full_manual.tflite"
VOCAB_SIZE = 30522
MAX_SEQUENCE_LENGTH = 512
CONFIDENCE_THRESHOLD = 0.75

// Special Tokens
UNK_TOKEN = "[UNK]"
CLS_TOKEN = "[CLS]" 
SEP_TOKEN = "[SEP]"
PAD_TOKEN = "[PAD]"
MASK_TOKEN = "[MASK]"
```

#### **Input/Output Shapes**
- **Input**: `[1, 512]` - Batch size 1, sequence length 512
- **Output**: `[1, 3]` - Batch size 1, 3 sentiment classes
- **Classes**: `["negative", "neutral", "positive"]`

### 🚀 Performance Characteristics

#### **Expected Inference Times**
- **LiteRT with GPU**: ~50ms
- **LiteRT with NPU**: ~80ms
- **TensorFlow Lite with GPU**: ~70ms
- **TensorFlow Lite with NNAPI**: ~100ms
- **TensorFlow Lite CPU**: ~200ms
- **Keyword Fallback**: ~10ms (lower accuracy)

#### **Hardware Requirements**
- **Minimum**: Android API 24+, 2GB RAM, CPU with XNNPACK
- **Recommended**: Android API 28+, 4GB RAM, GPU with OpenGL ES 3.1+, Google Play Services, NPU support

## 🧪 Testing Strategy

### 📊 Testing Pyramid

```
        /\
       /  \     UI Tests (Few)
      /____\    
     /      \   Integration Tests (Some)
    /________\  
   /          \ Unit Tests (Many)
  /____________\
```

### 🎯 Unit Tests

- **ViewModels** - Business logic testing
- **Repositories** - Data access testing
- **ML Components** - Adaptive runtime testing
- **Use Cases** - Business operations testing

### 🔗 Integration Tests

- **Repository + MCP Client** - Integration testing
- **ViewModel + Repository** - Layer interaction testing
- **ML Runtime Selection** - Hardware detection testing

### 📱 UI Tests

- **Screen Navigation** - Navigation testing
- **User Interactions** - User action testing
- **State Updates** - UI update testing
- **ML Integration** - Sentiment analysis testing

## 📏 Naming Conventions

### 🏷️ Classes

- **ViewModel**: `{Feature}ViewModel` (e.g., `MoviesListViewModel`)
- **Intent**: `{Feature}Intent` (e.g., `MoviesListIntent`)
- **State**: `{Feature}State` (e.g., `MoviesListState`)
- **Repository**: `{Feature}Repository` (e.g., `MovieRepository`)
- **Screen**: `{Feature}Screen` (e.g., `MoviesListScreen`)

### 📁 Folders

- **UI**: `ui/{feature}` (e.g., `ui/movieslist`)
- **Presentation**: `presentation/{feature}` (e.g., `presentation/movieslist`)
- **Data**: `data/{layer}` (e.g., `data/repository`)
- **ML**: `ml/{component}` (e.g., `ml/model`)

### 🔤 Variables and Functions

- **ViewModel state**: `_state`, `state`
- **Intent processing**: `processIntent(intent: Intent)`
- **Repository methods**: `get{Entity}()`, `save{Entity}()`
- **ML methods**: `analyzeSentiment()`, `detectHardware()`

## 🚀 Performance Considerations

### ⚡ Optimizations

1. **StateFlow vs LiveData** - Using StateFlow for Kotlin-first approach
2. **Lazy Loading** - Loading data on demand
3. **Image Caching** - Image caching with Coil
4. **Pagination** - Pagination for large lists
5. **ML Optimization** - Hardware-accelerated inference

### 📱 Memory Management

1. **ViewModel Scope** - Proper lifecycle management
2. **Coroutine Scope** - Canceling coroutines when ViewModel is destroyed
3. **Resource Cleanup** - Resource cleanup
4. **ML Model Management** - Proper model lifecycle

## 🔒 Security

### 🛡️ Data Security

1. **API Keys** - Storage in BuildConfig
2. **HTTPS** - Using secure connections
3. **Input Validation** - User input validation
4. **Error Handling** - Safe error handling
5. **ML Model Security** - Local model processing

## 📚 Best Practices

### ✅ Recommended

1. **Single Responsibility** - Each class has one responsibility
2. **Dependency Inversion** - Depend on abstractions, not implementations
3. **Immutable State** - Immutable state
4. **Unidirectional Flow** - Unidirectional data flow
5. **Error Handling** - Handling all possible errors
6. **Hardware Detection** - Adaptive ML runtime selection

### ❌ Not Recommended

1. **Business Logic in UI** - Business logic in UI layer
2. **Direct API Calls** - Direct API calls from ViewModel
3. **Mutable State** - Mutable state
4. **Tight Coupling** - Tight coupling between layers
5. **Hardcoded ML Runtime** - Fixed ML runtime selection

## 🔄 Migration Guide

### 📱 From MVVM to MVI

1. **Replace LiveData with StateFlow**
2. **Create Intent classes**
3. **Create State classes**
4. **Implement processIntent method**
5. **Update UI to observe state**

### 🏗️ From Monolithic to Modular

1. **Identify feature boundaries**
2. **Create feature modules**
3. **Extract shared dependencies**
4. **Update build configuration**
5. **Test module integration**

---

**Last Updated**: 2025-01-27  
**Document Version**: 3.0.0  
**Status**: Current