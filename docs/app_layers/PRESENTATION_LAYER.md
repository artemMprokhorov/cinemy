# Presentation Layer

MVI pattern implementation for Cinemy Android app with Jetpack Compose and data layer integration.

## Structure

```
presentation/
├── movieslist/               # Movies list screen
│   ├── MoviesListState.kt    # State data class
│   ├── MoviesListIntent.kt   # Intent sealed class
│   └── MoviesListViewModel.kt # ViewModel with MVI pattern
├── moviedetail/              # Movie details screen
│   ├── MovieDetailState.kt   # State data class
│   ├── MovieDetailIntent.kt  # Intent sealed class
│   └── MovieDetailViewModel.kt # ViewModel with MVI pattern
├── di/                       # Dependency injection
│   └── PresentationModule.kt # Koin module for ViewModels
└── PresentationConstants.kt  # Centralized constants
```

## MVI Data Flow

```
User Action → Intent → ViewModel.processIntent() → Repository → State Update → UI Update
```

**Flow Example:**
```kotlin
// 1. User action
viewModel.processIntent(MoviesListIntent.LoadPopularMovies)

// 2. ViewModel processes intent
when (intent) {
    is MoviesListIntent.LoadPopularMovies -> loadMovies()
}

// 3. Repository call
val result = movieRepository.getPopularMovies(page)

// 4. State update
_state.value = _state.value.copy(movies = result.data.movies)

// 5. UI updates automatically
val state by viewModel.state.collectAsState()
```

## Quick Start

```kotlin
@Composable
fun MoviesListScreen(
    viewModel: MoviesListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    when {
        state.isLoading -> LoadingContent()
        state.error != null -> ErrorContent(
            onRetry = { viewModel.processIntent(MoviesListIntent.Retry) }
        )
        else -> MoviesContent(
            state = state,
            onIntent = { viewModel.processIntent(it) }
        )
    }
}
```

## Key Components

### MoviesListScreen
- **MoviesListState**: UI state with movies, loading, error states
- **MoviesListIntent**: User interactions (LoadPopularMovies, SearchMovies, etc.)
- **MoviesListViewModel**: MVI pattern implementation with repository integration
- **Files**: `presentation/movieslist/`

### MovieDetailScreen  
- **MovieDetailState**: Movie details with computed formatting properties
- **MovieDetailIntent**: Detail screen interactions (LoadMovie, AnalyzeSentiment)
- **MovieDetailViewModel**: MVI with ML sentiment analysis integration
- **Files**: `presentation/moviedetail/`

### Dependency Injection
- **PresentationModule**: Koin module providing ViewModels
- **File**: `presentation/di/PresentationModule.kt`

### Constants
- **PresentationConstants**: Default values, formatting, UI configuration
- **File**: `presentation/PresentationConstants.kt`

## Architecture Decisions

**MVI Pattern**: Ensures unidirectional data flow and clear separation of concerns
- Intent → ViewModel → State → UI
- StateFlow for reactive updates
- Immutable state objects

**Repository Integration**: Clean data layer access through dependency injection
- Type-safe Result handling
- Suspend functions for async operations
- UI configuration from backend responses

**Jetpack Compose**: Modern declarative UI with state-driven updates
- Koin integration with `koinViewModel()`
- Automatic UI updates via StateFlow
- Separation of UI and business logic

**ML Integration**: SentimentAnalyzer for AI-powered features
- Backend-provided sentiment reviews
- ML-specific error handling
- Computed formatting properties

## Configuration

**Dependencies**: Koin, Coroutines, StateFlow, Jetpack Compose, Navigation Compose, ML Integration

**Navigation**: Type-safe navigation with parameter passing
- Screen.List → Screen.Details
- Deep linking support

**Error Handling**: Graceful recovery with retry mechanisms
- Connection status tracking
- Mock data detection
- User-friendly error messages

## FAQ

**Q: How to add new screen?**
A: Create package with State, Intent, ViewModel classes following MVI pattern

**Q: How to handle errors?**
A: Use Result sealed class and retry mechanisms in ViewModels

**Q: How to test ViewModels?**
A: Test intent processing and state updates with mock repositories

**Q: How to customize UI?**
A: Modify PresentationConstants or use backend-driven theming via uiConfig
