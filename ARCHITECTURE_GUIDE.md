# ARCHITECTURE_GUIDE.md

**Cinemy - Architecture Guide**  
**Created**: 2025-09-19  
**Version**: 1.0.0-dev

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
└── utils/                       # Utilities
```

### 📱 UI Layer

**Purpose**: UI display and user interaction handling

**Principles**:
- UI logic only
- Reacting to state changes
- Sending Intents to ViewModel

**Components**:
- `MoviesListScreen` - Movie list screen
- `MovieDetailScreen` - Movie details screen
- `MovieAppSplashScreen` - Splash screen
- `components/` - Reusable UI components

### 🎭 Presentation Layer

**Purpose**: State management and UI business logic

**Principles**:
- Intent processing
- State management
- Repository interaction

**Components**:
- ViewModels - State management
- Intent Classes - User action description
- State Classes - UI state description

### 💾 Data Layer

**Purpose**: Data management and external APIs

**Principles**:
- Repository pattern
- Data source abstraction
- Caching

**Components**:
- `MovieRepository` - Repository interface
- `MovieRepositoryImpl` - Repository implementation
- `McpClient` - MCP client
- `McpHttpClient` - HTTP client

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

### 🔗 Dependency Graph

```
MainActivity
    ↓
AppNavigation
    ↓
Screens → ViewModels → Repository → MCP Client
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

### 🎨 Dynamic Color Architecture

#### **Color Flow Process:**

1. **Backend Response** → Contains `uiConfig` with dynamic colors
2. **McpClient** → Extracts `uiConfig` from response JSON
3. **MovieMapper** → Converts DTO colors to Compose Color objects
4. **ViewModel** → Passes `uiConfig` to UI state
5. **UI Components** → Apply dynamic colors to elements

#### **Color Extraction Logic:**

```kotlin
// Backend Response Structure
{
  "data": { "movieDetails": {...} },
  "uiConfig": {
    "colors": {
      "primary": "#DC3528",      // Movie-specific primary color
      "secondary": "#E64539",     // Movie-specific secondary color
      "background": "#121212",   // Dark theme background
      "surface": "#1E1E1E"       // Dark theme surface
    }
  }
}
```

#### **Dynamic Color Application:**

- **Rating Colors**: `uiConfig.colors.primary` for movie ratings
- **Background Colors**: `uiConfig.colors.background` for screen backgrounds
- **Surface Colors**: `uiConfig.colors.surface` for card surfaces
- **Text Colors**: `uiConfig.colors.onSurface/onBackground` for text

#### MCP HTTP Client
```kotlin
class McpHttpClient {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }
    
    suspend fun getPopularMovies(): McpResponse {
        return client.get("$baseUrl/movies/popular").body()
    }
}
```

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
- **Use Cases** - Business operations testing

### 🔗 Integration Tests

- **Repository + MCP Client** - Integration testing
- **ViewModel + Repository** - Layer interaction testing

### 📱 UI Tests

- **Screen Navigation** - Navigation testing
- **User Interactions** - User action testing
- **State Updates** - UI update testing

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

### 🔤 Variables and Functions

- **ViewModel state**: `_state`, `state`
- **Intent processing**: `processIntent(intent: Intent)`
- **Repository methods**: `get{Entity}()`, `save{Entity}()`

## 🤖 Machine Learning Architecture

### 🧠 ML Model Integration

Cinemy features a sophisticated **hybrid ML system** that combines production-grade TensorFlow Lite models with keyword-based fallback for robust sentiment analysis.

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Text Input    │───▶│  SentimentAnalyzer │───▶│  SentimentResult │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                │
                                ▼
                    ┌─────────────────────┐
                    │  Model Selection    │
                    │  Logic              │
                    └─────────────────────┘
                                │
                    ┌───────────┴───────────┐
                    ▼                       ▼
        ┌─────────────────────┐    ┌─────────────────────┐
        │ TensorFlowSentiment │    │ KeywordSentiment    │
        │ Model (BERT)        │    │ Model (Fallback)    │
        │                     │    │                     │
        │ • 3.8MB Model       │    │ • Fast Processing   │
        │ • 30,522 Vocab      │    │ • Rule-based        │
        │ • 95%+ Accuracy     │    │ • Reliable Fallback │
        │ • BERT Tokenization │    │ • No Dependencies   │
        └─────────────────────┘    └─────────────────────┘
```

### 🔄 ML Data Flow

1. **Text Input** → Raw text from movie reviews
2. **Model Selection** → Choose between TensorFlow Lite or keyword model
3. **Text Preprocessing** → BERT tokenization or keyword extraction
4. **Model Inference** → Run sentiment analysis
5. **Result Processing** → Apply confidence thresholds
6. **Fallback Logic** → Switch to keyword model if needed
7. **Sentiment Result** → Return structured sentiment data

### 🏗️ ML Components

#### **TensorFlowSentimentModel**
- **Purpose**: Production BERT-based sentiment analysis
- **Model**: `production_sentiment_full_manual.tflite` (3.8MB)
- **Input**: 512-token sequences with BERT tokenization
- **Output**: 3-class sentiment classification
- **Performance**: NNAPI/XNNPACK acceleration enabled

#### **SentimentAnalyzer (Hybrid Coordinator)**
- **Purpose**: Orchestrates ML model selection and fallback
- **Logic**: Complex text → TensorFlow, Simple text → Keyword
- **Fallback**: Automatic fallback on ML errors
- **Caching**: Optional result caching for performance

#### **KeywordSentimentModel**
- **Purpose**: Fast, reliable fallback sentiment analysis
- **Method**: Rule-based keyword matching
- **Dependencies**: None (pure Kotlin)
- **Performance**: Sub-millisecond processing

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

### 🔧 ML Configuration

#### **TensorFlow Lite Settings**
```json
{
  "tensorflow_lite": {
    "model_file": "production_sentiment_full_manual.tflite",
    "model_type": "bert_sentiment_analysis",
    "input_config": {
      "input_shape": [1, 512],
      "input_type": "int32",
      "vocab_size": 30522
    },
    "output_config": {
      "output_shape": [1, 3],
      "confidence_threshold": 0.75
    },
    "performance": {
      "use_nnapi": true,
      "enable_xnnpack": true,
      "num_threads": 4
    }
  }
}
```

### 🚀 Performance Considerations

#### **ML Optimizations**
- **Model Quantization**: 3.8MB optimized BERT model
- **Hardware Acceleration**: NNAPI and XNNPACK enabled
- **Memory Management**: Efficient tensor allocation
- **Fallback Strategy**: Graceful degradation on errors
- **Caching**: Optional result caching for repeated queries

#### **Error Handling**
- **Model Loading**: Graceful fallback if TensorFlow model fails
- **Inference Errors**: Automatic switch to keyword model
- **Vocabulary Issues**: Fallback vocabulary for missing tokens
- **Performance Monitoring**: ML performance tracking and logging

## 🚀 Performance Considerations

### ⚡ Optimizations

1. **StateFlow vs LiveData** - Using StateFlow for Kotlin-first approach
2. **Lazy Loading** - Loading data on demand
3. **Image Caching** - Image caching with Coil
4. **Pagination** - Pagination for large lists

### 📱 Memory Management

1. **ViewModel Scope** - Proper lifecycle management
2. **Coroutine Scope** - Canceling coroutines when ViewModel is destroyed
3. **Resource Cleanup** - Resource cleanup

## 🔒 Security

### 🛡️ Data Security

1. **API Keys** - Storage in BuildConfig
2. **HTTPS** - Using secure connections
3. **Input Validation** - User input validation
4. **Error Handling** - Safe error handling

## 📚 Best Practices

### ✅ Recommended

1. **Single Responsibility** - Each class has one responsibility
2. **Dependency Inversion** - Depend on abstractions, not implementations
3. **Immutable State** - Immutable state
4. **Unidirectional Flow** - Unidirectional data flow
5. **Error Handling** - Handling all possible errors

### ❌ Not Recommended

1. **Business Logic in UI** - Business logic in UI layer
2. **Direct API Calls** - Direct API calls from ViewModel
3. **Mutable State** - Mutable state
4. **Tight Coupling** - Tight coupling between layers

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

**Last Updated**: 2025-09-19  
**Document Version**: 1.0.0  
**Status**: Current
