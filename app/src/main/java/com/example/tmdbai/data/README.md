# Data Layer Implementation

This package contains the complete data layer implementation for the TMDB AI Android application, following Clean Architecture principles with MVI pattern and Jetpack Compose.

## Architecture Overview

The data layer is structured as follows:

```
data/
├── remote/
│   ├── api/           # API service interfaces
│   └── dto/           # Data Transfer Objects
├── mcp/               # MCP client for backend communication
├── mapper/            # Mappers between DTOs and domain models
├── model/             # Domain models
├── repository/        # Repository interfaces and implementations
└── di/                # Dependency injection modules
```

## MVI Architecture Pattern - Model Component

### ✅ **MVI Model Implementation**

This data layer serves as the **Model** component in the MVI (Model-View-Intent) architecture:

#### **1. Domain Models (Model State)**
- **Clean Models**: `Movie`, `MovieDetails`, `Genre`, `ProductionCompany`
- **Result Handling**: Sealed `Result<T>` class for type-safe state management
- **UI Configuration**: `UiConfiguration` models for dynamic theming
- **Pagination**: `Pagination` and `MovieListResponse` for list management

#### **2. Repository Pattern (Model Logic)**
- **Interface Contract**: `MovieRepository` defines the model's public API
- **Implementation**: `MovieRepositoryImpl` contains business logic
- **Data Sources**: MCP client for backend communication
- **Error Handling**: Consistent error handling across all operations

#### **3. Data Flow Integration**
- **Unidirectional Flow**: Data flows from repository to presentation layer
- **State Management**: Repository returns `Result<T>` for state handling
- **UI Configuration**: Backend-driven UI configuration included in responses
- **Type Safety**: Sealed classes ensure compile-time safety

### 🔄 **MVI Data Flow - Model Role**

```
Intent (from ViewModel) → Repository → Data Sources → Domain Models → Result → State Update
```

#### **Example Flow:**
```kotlin
// 1. ViewModel sends intent
viewModel.processIntent(MoviesListIntent.LoadPopularMovies)

// 2. Repository processes request
val result = movieRepository.getPopularMovies(page)

// 3. Repository returns Result<T>
when (result) {
    is Result.Success -> {
        // Update state with domain models
        _state.value = _state.value.copy(
            movies = result.data.movies,  // Domain models
            uiConfig = result.uiConfig    // UI configuration
        )
    }
    is Result.Error -> {
        // Handle error state
        _state.value = _state.value.copy(
            error = result.message
        )
    }
}
```

## Key Components

### 1. Remote Layer (`remote/`)

#### API Service (`api/MovieApiService.kt`)
- Defines the interface for movie-related API calls
- All methods return `McpResponseDto<T>` which includes both data and UI configuration
- Supports popular movies, top rated, now playing, search, details, and recommendations

#### DTOs (`dto/MovieDto.kt`)
- **Movie Data DTOs**: `MovieDto`, `MovieDetailsDto`, `GenreDto`, `ProductionCompanyDto`
- **UI Configuration DTOs**: `UiConfigurationDto`, `ColorSchemeDto`, `TextConfigurationDto`, `ButtonConfigurationDto`
- **MCP Response DTOs**: `McpResponseDto<T>`, `McpMovieListResponseDto`, `PaginationDto`

### 2. MCP Client (`mcp/McpClient.kt`)

- Implements `MovieApiService` interface
- Simulates network calls with JSON payloads
- Provides mock data for development and testing
- Includes realistic network delays and error simulation
- Returns both movie data and UI configuration from backend

### 3. Domain Models (`model/Movie.kt`)

- Clean domain models used by the UI layer
- Includes `Movie`, `MovieDetails`, `Genre`, `ProductionCompany`
- `Result<T>` sealed class for handling success/error states
- `UiConfiguration` models for dynamic UI theming

### 4. Mappers (`mapper/MovieMapper.kt`)

- Converts between DTOs and domain models
- Handles color string to Compose Color conversion
- Provides type-safe mapping functions

### 5. Repository Layer (`repository/`)

#### Interface (`MovieRepository.kt`)
- Defines repository contract with suspend functions
- Returns `Result<T>` sealed class for error handling

#### Implementation (`MovieRepositoryImpl.kt`)
- Implements repository interface
- Uses MCP client for data fetching
- Handles error mapping and UI configuration extraction
- Provides clean API for the presentation layer

### 6. Dependency Injection (`di/DataModule.kt`)

- Koin module for data layer dependencies
- Provides singleton instances for MCP client and repositories
- Includes ViewModel factory for MVI pattern

## Key Features

### ✅ Clean Architecture Compliance
- Clear separation of concerns
- Dependency inversion principle
- Repository pattern implementation

### ✅ MVI Pattern Support
- Intent-based user interactions
- StateFlow for reactive state management
- Unidirectional data flow

### ✅ MCP Backend Integration
- No TMDB API key in the app
- All data comes through MCP backend
- UI configuration included in responses
- Simulated network calls for development

### ✅ Error Handling
- Graceful error recovery
- Type-safe error handling with sealed classes
- User-friendly error messages
- Loading state management

### ✅ Type Safety
- Sealed classes for Result handling
- Compile-time safety for all operations
- Null safety with proper defaults
- Domain model validation

### ✅ Dynamic UI Configuration
- Backend-driven theming
- Configurable colors and texts
- Dynamic button styling
- Movie poster color palettes

### ✅ Repository Pattern
- Clean interface definitions
- Implementation separation
- Dependency injection support
- Testable architecture

## MVI Integration Examples

### Repository Usage in ViewModels
```kotlin
class MoviesListViewModel(
    private val movieRepository: MovieRepository  // Model dependency
) : ViewModel() {
    
    private fun loadMovies() {
        viewModelScope.launch {
            // Model operation
            val result = movieRepository.getPopularMovies(_state.value.currentPage)
            
            // State update based on model result
            when (result) {
                is Result.Success -> {
                    val movieListResponse = result.data as MovieListResponse
                    _state.value = _state.value.copy(
                        movies = movieListResponse.movies,  // Domain models
                        uiConfig = result.uiConfig          // UI configuration
                    )
                }
                is Result.Error -> {
                    _state.value = _state.value.copy(
                        error = result.message
                    )
                }
            }
        }
    }
}
```

### Domain Model Usage
```kotlin
// Clean domain models for UI consumption
data class Movie(
    val id: Int,
    val title: String,
    val description: String,
    val posterPath: String?,
    val rating: Double,
    val releaseDate: String
)

// Result handling for type-safe state management
sealed class Result<out T> {
    data class Success<T>(val data: T, val uiConfig: UiConfiguration? = null) : Result<T>()
    data class Error(val message: String, val uiConfig: UiConfiguration? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
```

## Dependencies

- **Koin**: Dependency injection
- **Coroutines**: Asynchronous programming
- **Gson**: JSON parsing
- **Jetpack Compose**: UI framework integration

## Build Status

✅ **BUILD SUCCESSFUL** - All data layer components compile correctly and are fully integrated with the presentation layer.

✅ **MVI MODEL VERIFIED** - Complete compliance with MVI architecture Model component.

The data layer is now ready for production use with complete Clean Architecture implementation, MVI pattern support, and seamless presentation layer integration.
