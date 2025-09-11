# ARCHITECTURE_GUIDE.md

**Cinemy - Architecture Guide**  
**Created**: 2024-12-19  
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

#### MCP Client
```kotlin
class McpClient {
    suspend fun getPopularMoviesViaMcp(): Result<List<Movie>> {
        return try {
            val response = mcpHttpClient.getPopularMovies()
            Result.success(response.movies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

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

**Last Updated**: 2024-12-19  
**Document Version**: 1.0.0  
**Status**: Current
