# Presentation Layer Implementation

This package contains the complete presentation layer implementation for the TMDB AI Android
application, following MVI pattern with Jetpack Compose and integrating with the data layer.

## Architecture Overview

The presentation layer is structured as follows:

```
presentation/
├── commons/                   # Common base classes
│   └── CommonIntent.kt       # Base interface for all intents
├── movieslist/               # Movies list screen implementation
│   ├── MoviesListState.kt    # State data class
│   ├── MoviesListIntent.kt   # Intent sealed class
│   └── MoviesListViewModel.kt # ViewModel with MVI pattern
├── moviedetail/              # Movie details screen implementation
│   ├── MovieDetailState.kt   # State data class
│   ├── MovieDetailIntent.kt  # Intent sealed class
│   └── MovieDetailViewModel.kt # ViewModel with MVI pattern
└── di/                       # Dependency injection modules
    └── PresentationModule.kt # Koin module for ViewModels
```

## MVI Architecture Pattern Implementation

### ✅ **MVI Pattern Compliance Verification**

This implementation **fully complies with MVI (Model-View-Intent) architecture principles**:

#### **1. MODEL (Data Layer Integration)**

- **Repository Pattern**: Clean integration with `MovieRepository`
- **Domain Models**: Uses clean domain models from data layer
- **Result Handling**: Type-safe `Result<T>` sealed class
- **Business Logic**: Encapsulated in repository layer

#### **2. VIEW (UI Layer)**

- **Jetpack Compose**: Modern declarative UI components
- **State-Driven**: UI reacts to state changes automatically
- **Intent Sending**: UI sends intents to ViewModels
- **Separation**: UI only handles display logic

#### **3. INTENT (User Interactions)**

- **Sealed Classes**: Type-safe intent definitions
- **Comprehensive Coverage**: All user interactions captured
- **Interface Inheritance**: Proper `CommonIntent` interface structure
- **Search Support**: Complete search functionality with `SearchMovies`, `ClearSearch`
- **Examples**: `LoadPopularMovies`, `SearchMovies`, `LoadMoreMovies`, `RetryLastOperation`

#### **4. STATE (UI State Management)**

- **Immutable State**: Data classes with default values
- **StateFlow**: Reactive state management
- **Comprehensive State**: All UI state in single object
- **Search State**: Complete search state management with metadata
- **Screen Modes**: Support for POPULAR and SEARCH screen modes
- **UI Configuration**: Dynamic theming support

#### **5. VIEWMODEL (Business Logic)**

- **Intent Processing**: `processIntent()` method
- **State Management**: `StateFlow` for reactive updates
- **Unidirectional Flow**: Intent → Reducer → New State
- **Repository Integration**: Clean data layer access

### 🔄 **MVI Data Flow**

#### **Unidirectional Data Flow:**

```
User Action → Intent → ViewModel.processIntent() → Repository → State Update → UI Update
```

#### **Detailed Flow Example:**

```kotlin
// 1. User clicks "Load Popular Movies"
viewModel.processIntent(MoviesListIntent.LoadPopularMovies)

// 2. ViewModel processes intent
when (intent) {
    is MoviesListIntent.LoadPopularMovies -> {
        currentMovieType = MovieType.POPULAR
        loadMovies()
    }
}

// 3. Repository call
val result = movieRepository.getPopularMovies(page)

// 4. State update
_state.value = _state.value.copy(
    movies = result.data.movies,
    isLoading = false,
    uiConfig = result.uiConfig
)

// 5. UI automatically updates via StateFlow
val state by viewModel.state.collectAsState()
```

### ✅ **MVI Pattern Compliance Checklist**

| MVI Component              | Status | Implementation                                  |
|----------------------------|--------|-------------------------------------------------|
| **Model**                  | ✅      | Data layer with repositories and business logic |
| **View**                   | ✅      | Jetpack Compose UI components                   |
| **Intent**                 | ✅      | Sealed classes for user interactions            |
| **State**                  | ✅      | Immutable data classes with StateFlow           |
| **Unidirectional Flow**    | ✅      | Intent → Reducer → State → UI                   |
| **State Management**       | ✅      | StateFlow for reactive updates                  |
| **Separation of Concerns** | ✅      | Clear boundaries between layers                 |
| **Type Safety**            | ✅      | Sealed classes and Result types                 |
| **Error Handling**         | ✅      | Result sealed class with error states           |
| **Reactive UI**            | ✅      | Automatic UI updates via StateFlow              |

## Key Components

### 1. Common Layer (`commons/`)

#### CommonIntent.kt

- **CommonIntent**: Base interface for all intents
    - `Retry`: Retry the current operation
    - `Refresh`: Refresh the current data
    - `BackPressed`: Handle back button press
- **Inheritance**: All specific intents implement this interface

### 2. Movies List Screen (`movieslist/`)

#### MoviesListState.kt

- **MoviesListState**: Data class containing all UI state information
    - `movies`: List of movies to display
    - `isLoading`: Loading state indicator
    - `error`: Error message if any
    - `uiConfig`: Dynamic UI configuration from backend
    - `currentPage`: Current pagination page
    - `totalPages`: Total number of pages
    - `hasNextPage`/`hasPreviousPage`: Pagination controls
    - `searchQuery`: Current search query
    - `isSearchMode`: Search mode toggle

#### MoviesListIntent.kt

- **MoviesListIntent**: Sealed class defining all user interactions
    - `LoadPopularMovies`: Load popular movies
    - `LoadTopRatedMovies`: Load top-rated movies
    - `LoadNowPlayingMovies`: Load now-playing movies
    - `SearchMovies(query)`: Search movies with query
    - `LoadNextPage`: Load next page of results
    - `LoadPreviousPage`: Load previous page of results
    - `MovieClicked(movieId)`: Handle movie selection
    - `ClearSearch`: Clear search query
    - `ToggleSearchMode`: Toggle search mode
    - `Retry`: Retry failed operation
    - `Refresh`: Refresh current data
    - `BackPressed`: Handle back button press

#### MoviesListViewModel.kt

- **MoviesListViewModel**: Implements MVI pattern
- **Intent Processing**: Handles all user interactions through intents
- **State Management**: Exposes StateFlow for reactive UI updates
- **Repository Integration**: Uses MovieRepository for data fetching
- **Pagination Support**: Handles next/previous page loading
- **Search Functionality**: Supports movie search with query
- **Error Handling**: Graceful error recovery with retry functionality

### 3. Movie Details Screen (`moviedetail/`)

#### MovieDetailState.kt

- **MovieDetailState**: Data class containing movie details state
    - `movieDetails`: Complete movie information
    - `isLoading`: Loading state indicator
    - `error`: Error message if any
    - `uiConfig`: Dynamic UI configuration from backend

#### MovieDetailIntent.kt

- **MovieDetailIntent**: Sealed class defining all user interactions
    - `LoadMovieDetails(movieId)`: Load specific movie details
    - `LoadRecommendations`: Load movie recommendations
    - `Retry`: Retry failed operation
    - `Refresh`: Refresh current data
    - `BackPressed`: Handle back button press

#### MovieDetailViewModel.kt

- **MovieDetailViewModel**: Implements MVI pattern for details
- **Intent Processing**: Handles movie details loading and retry
- **State Management**: Exposes StateFlow for reactive UI updates
- **Repository Integration**: Uses MovieRepository for data fetching
- **Error Handling**: Graceful error recovery with retry functionality

### 4. Dependency Injection (`di/`)

#### PresentationModule.kt

- **Koin Module**: Provides ViewModels for dependency injection
- **ViewModel Factories**: Creates ViewModels with repository dependencies
- **Integration**: Works with data layer modules

## MVI Pattern Implementation

### Intent → Reducer → New State Flow

1. **Intent**: User interactions are captured as intents
   ```kotlin
   viewModel.processIntent(MoviesListIntent.LoadPopularMovies)
   ```

2. **Reducer**: ViewModel processes intents and updates state
   ```kotlin
   when (intent) {
       is MoviesListIntent.LoadPopularMovies -> {
           currentMovieType = MovieType.POPULAR
           loadMovies()
       }
   }
   ```

3. **State**: UI reacts to state changes through StateFlow
   ```kotlin
   val state by viewModel.state.collectAsState()
   ```

### State Management

- **StateFlow**: Reactive state management with automatic UI updates
- **Immutable State**: State objects are immutable data classes
- **State Copying**: Updates create new state instances
- **UI Configuration**: Dynamic theming from backend responses

## Navigation Integration

### Screen Flow

1. **Splash** → **MoviesListScreen**
2. **MoviesListScreen** → **MovieDetailScreen** (with movie ID)

### Navigation Implementation

```kotlin
// In Navigation.kt
composable(Screen.List.route) {
    MoviesListScreen(
        onMovieClick = { movieId ->
            navController.navigate(Screen.Details.createRoute(movieId.toString()))
        }
    )
}

composable(Screen.Details.route) { backStackEntry ->
    val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull() ?: 1
    MovieDetailScreen(
        movieId = movieId,
        onBackClick = { navController.popBackStack() }
    )
}
```

## Data Layer Integration

### Repository Usage

- **Dependency Injection**: ViewModels receive repositories through Koin
- **Suspend Functions**: All repository calls are suspend functions
- **Result Handling**: Uses sealed Result class for type-safe error handling
- **UI Configuration**: Extracts UI configuration from repository responses

### Example Integration

```kotlin
class MoviesListViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {
    
    private fun loadMovies() {
        viewModelScope.launch {
            val result = movieRepository.getPopularMovies(_state.value.currentPage)
            
            when (result) {
                is Result.Success -> {
                    val movieListResponse = result.data as MovieListResponse
                    _state.value = _state.value.copy(
                        movies = movieListResponse.movies,
                        uiConfig = result.uiConfig
                    )
                }
                is Result.Error -> {
                    _state.value = _state.value.copy(
                        error = result.message,
                        uiConfig = result.uiConfig
                    )
                }
            }
        }
    }
}
```

## Key Features

### ✅ MVI Pattern Compliance

- Intent-based user interactions
- Unidirectional data flow
- StateFlow for reactive updates
- Clear separation of concerns

### ✅ Organized Package Structure

- Each ViewModel has its own package
- Intent and State classes co-located with ViewModels
- Common base classes in dedicated commons package
- Clear separation of concerns

### ✅ Jetpack Compose Integration

- Modern declarative UI
- State-driven composables
- Compose-friendly ViewModels
- Koin integration with `koinViewModel()`

### ✅ Dynamic UI Configuration

- Backend-driven theming
- Configurable colors and texts
- Dynamic button styling
- Movie poster color palettes

### ✅ Error Handling

- Graceful error recovery
- Retry functionality
- User-friendly error messages
- Loading state management

### ✅ Search and Pagination

- Movie search functionality
- Pagination controls
- Search mode toggle
- Query persistence

### ✅ Navigation

- Type-safe navigation
- Parameter passing
- Back stack management
- Deep linking support

## Usage Example

```kotlin
@Composable
fun MoviesListScreen(
    onMovieClick: (Int) -> Unit = {},
    onBackPressed: () -> Unit = {},
    viewModel: MoviesListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(state.uiConfig?.colors?.background ?: AppBackground)
    ) {
        when {
            state.isLoading -> LoadingContent()
            state.error != null -> ErrorContent(
                error = state.error!!,
                onRetry = { viewModel.processIntent(MoviesListIntent.Retry) }
            )
            else -> MoviesContent(
                state = state,
                onMovieClick = onMovieClick,
                onIntent = { viewModel.processIntent(it) }
            )
        }
    }
}
```

## Dependencies

- **Koin**: Dependency injection
- **Coroutines**: Asynchronous programming
- **StateFlow**: Reactive state management
- **Jetpack Compose**: UI framework
- **Navigation Compose**: Navigation

## Build Status

✅ **BUILD SUCCESSFUL** - All presentation layer components compile correctly and are fully
integrated with the data layer.

✅ **MVI PATTERN VERIFIED** - Complete compliance with MVI architecture principles.

The presentation layer is now ready for production use with complete MVI pattern implementation,
dynamic UI configuration, and seamless data layer integration.
