# Data Layer Implementation

This package contains the complete data layer implementation for the Cinemy Android application,
following Clean Architecture principles with MVI pattern and Jetpack Compose.

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
├── util/              # Utility classes for data processing
└── di/                # Dependency injection modules
```

## MVI Architecture Pattern - Model Component

### ✅ **MVI Model Implementation**

This data layer serves as the **Model** component in the MVI (Model-View-Intent) architecture:

#### **1. Domain Models (Model State)**

- **Clean Models**: `Movie`, `MovieDetails`, `Genre`, `ProductionCompany`
- **Enhanced Movie Model**: Added `backdropPath`, `voteCount`, `popularity`, `adult` fields
- **Complete MovieDetails**: Runtime, genres, production companies, budget, revenue
- **Search Metadata**: `SearchInfo` for search-specific UI configuration
- **AI Metadata**: `Meta` and `GeminiColors` for backend AI generation tracking
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

- **Interface Definition**: Defines the contract for movie-related API calls
- **Methods**: 
  - `getPopularMovies(page: Int)` - Fetches popular movies with pagination
  - `getMovieDetails(movieId: Int)` - Fetches detailed movie information
- **Return Type**: All methods return `McpResponseDto<T>` with data and UI configuration
- **MCP Integration**: Designed for MCP backend communication

#### DTOs (`dto/MovieDto.kt`)

- **Movie Data DTOs**: 
  - `MovieDto` - Basic movie information with colors and metadata
  - `MovieDetailsDto` - Complete movie details with genres, companies, sentiment data
  - `GenreDto`, `ProductionCompanyDto` - Supporting data structures
- **UI Configuration DTOs**: 
  - `UiConfigurationDto` - Complete UI configuration from backend
  - `ColorSchemeDto` - Color palette configuration
  - `TextConfigurationDto` - Text content configuration
  - `ButtonConfigurationDto` - Button styling configuration
  - `SearchInfoDto` - Search-specific UI configuration
- **MCP Response DTOs**: 
  - `McpResponseDto<T>` - Generic MCP response wrapper
  - `MovieListResponseDto` - Paginated movie list response
  - `SentimentReviewsDto`, `SentimentMetadataDto` - Sentiment analysis data
- **Color System DTOs**:
  - `MovieColorsDto` - Movie-specific color palette
  - `ColorMetadataDto` - Color analysis metadata
  - `GeminiColorsDto` - AI-generated color suggestions

### 2. MCP Client (`mcp/`)

#### McpClient (`McpClient.kt`)

- **Interface Implementation**: Implements `MovieApiService` interface
- **MCP Integration**: Uses `McpHttpClient` for backend communication
- **Asset Loading**: Uses `AssetDataLoader` for UI configuration and metadata
- **Methods**:
  - `getPopularMovies(page: Int)` - Fetches popular movies via MCP
  - `getMovieDetails(movieId: Int)` - Fetches movie details via MCP
  - `getPopularMoviesViaMcp(page: Int)` - Direct MCP call returning domain models
  - `getMovieDetailsViaMcp(movieId: Int)` - Direct MCP call returning domain models
- **Response Processing**: Converts MCP responses to DTOs and domain models
- **Error Handling**: Comprehensive error handling with fallback configurations

#### Supporting MCP Components

- **AssetDataLoader** (`AssetDataLoader.kt`) - Loads UI configuration and metadata from assets
- **McpHttpClient** (`McpHttpClient.kt`) - HTTP client for MCP communication
- **FakeInterceptor** (`FakeInterceptor.kt`) - Network interceptor for testing
- **MCP Models** (`models/`):
  - `McpRequest.kt` - MCP request structure
  - `McpResponse.kt` - MCP response structure

### 3. Domain Models (`model/`)

#### Core Models (`Movie.kt`)

- **Movie**: Complete movie information with colors and metadata
  - Basic info: id, title, description, posterPath, backdropPath
  - Ratings: rating, voteCount, popularity
  - Metadata: adult, originalLanguage, originalTitle, video
  - Colors: MovieColors with accent, primary, secondary, metadata
- **MovieDetails**: Extended movie information
  - Runtime, genres, production companies
  - Budget, revenue, status
  - Sentiment analysis data (reviews, metadata)
- **Supporting Models**: Genre, ProductionCompany, MovieColors, ColorMetadata

#### Response Models

- **MovieListResponse**: Paginated movie lists
  - page, results, totalPages, totalResults
- **MovieDetailsResponse**: Complete movie details wrapper
  - success, data, uiConfig, error, meta
- **MovieDetailsData**: Movie details with sentiment data
  - movieDetails, sentimentReviews, sentimentMetadata
- **Pagination**: Pagination information
  - page, totalPages, totalResults, hasNext, hasPrevious

#### UI Configuration Models

- **UiConfiguration**: Complete UI configuration
  - colors, texts, buttons, searchInfo
- **ColorScheme**: Color palette with Compose Color objects
  - primary, secondary, background, surface, onPrimary, etc.
- **TextConfiguration**: Text content configuration
  - appTitle, loadingText, errorMessage, button texts
- **ButtonConfiguration**: Button styling
  - colors, textColor, cornerRadius
- **SearchInfo**: Search-specific configuration
  - query, resultCount, avgRating, ratingType, colorBased

#### Metadata Models

- **Meta**: API response metadata
  - timestamp, method, searchQuery, movieId, resultsCount
  - aiGenerated, geminiColors, ratings, version
- **GeminiColors**: AI-generated color suggestions
  - primary, secondary, accent
- **SentimentReviews**: Sentiment analysis results
  - positive, negative review lists
  - hasPositiveReviews, hasNegativeReviews, hasAnyReviews computed properties
- **SentimentMetadata**: Sentiment analysis metadata
  - totalReviews, positiveCount, negativeCount, source, timestamp, apiSuccess

#### Additional Models

- **StringConstants**: Centralized string constants for data layer
  - Error messages, MCP method names, UI text constants
  - Serialization field names, default values, color constants
  - Network configuration, pagination constants

#### Result Handling

- **Result<T>**: Sealed class for type-safe state management
  - `Success<T>` - Contains data and optional UI config
  - `Error` - Contains error message and optional UI config
  - `Loading` - Loading state indicator

### 4. Mappers (`mapper/MovieMapper.kt`)

- **Purpose**: Converts between DTOs and domain models
- **Color Conversion**: Handles string to Compose Color conversion using ColorUtils
- **Type Safety**: Provides type-safe mapping functions
- **Key Mappings**:
  - `mapMovieDtoToMovie()` - MovieDto → Movie
  - `mapMovieDetailsDtoToMovieDetails()` - MovieDetailsDto → MovieDetails
  - `mapMovieListResponseDtoToMovieListResponse()` - MovieListResponseDto → MovieListResponse
  - `mapUiConfigurationDtoToUiConfiguration()` - UiConfigurationDto → UiConfiguration
  - `mapSentimentReviewsDtoToSentimentReviews()` - SentimentReviewsDto → SentimentReviews
  - `mapSentimentMetadataDtoToSentimentMetadata()` - SentimentMetadataDto → SentimentMetadata
  - Supporting mappings for Genre, ProductionCompany, ColorScheme, etc.

### 5. Repository Layer (`repository/`)

#### Interface (`MovieRepository.kt`)

- **Contract Definition**: Defines repository interface with suspend functions
- **Return Types**: All methods return `Result<T>` sealed class for error handling
- **Methods**:
  - `getPopularMovies(page: Int)` - Fetches popular movies with pagination
  - `getMovieDetails(movieId: Int)` - Fetches detailed movie information
- **Type Safety**: Uses sealed Result class for compile-time safety

#### Implementation (`MovieRepositoryImpl.kt`)

- **Dual Mode Support**: Routes between mock data and MCP client based on BuildConfig.USE_MOCK_DATA
- **MCP Integration**: Uses McpClient for backend communication
- **Mock Data Support**: Provides mock data for development and testing
- **Error Handling**: Comprehensive error handling with fallback configurations
- **UI Configuration**: Handles UI configuration extraction and mapping

#### Key Features

- **Build Variant Support**: 
  - Mock mode: Uses AssetDataLoader for local data
  - Production mode: Uses MCP client for backend data
- **Error Recovery**: Graceful error handling with user-friendly messages
- **UI Configuration**: Dynamic theming support from backend
- **Pagination**: Full pagination support for movie lists
- **Sentiment Analysis**: Support for sentiment reviews and metadata

### 6. Utility Classes (`util/`)

#### AssetUtils (`AssetUtils.kt`)

- **Asset Loading**: Utility functions for loading JSON data from assets
- **File Operations**: Helper methods for asset file management
- **Error Handling**: Safe asset loading with fallback mechanisms

#### ColorUtils (`ColorUtils.kt`)

- **Color Conversion**: Converts string colors to Compose Color objects
- **Color Parsing**: Handles hex color string parsing
- **Default Colors**: Provides fallback colors for invalid color strings
- **Color Validation**: Ensures color strings are valid before conversion

### 7. Dependency Injection (`di/DataModule.kt`)

- **Koin Module**: Dependency injection configuration for data layer
- **Singleton Management**: Provides singleton instances for MCP client and repositories
- **AssetDataLoader**: Dependency injection for asset data loading
- **Repository Binding**: Binds repository interface to implementation
- **MCP Client**: Provides MCP client instance for backend communication

## Key Features

### ✅ Clean Architecture Compliance

- **Separation of Concerns**: Clear boundaries between layers
- **Dependency Inversion**: Repository pattern with interface contracts
- **Single Responsibility**: Each component has a focused purpose
- **Testability**: Easy to mock and test individual components

### ✅ MVI Pattern Support

- **Model Component**: Data layer serves as the Model in MVI
- **Unidirectional Flow**: Data flows from repository to presentation layer
- **State Management**: Result<T> sealed class for type-safe state handling
- **Intent Processing**: Repository methods process intents from ViewModels

### ✅ MCP Backend Integration

- **No External APIs**: No TMDB API key required in the app
- **MCP Protocol**: All data comes through MCP backend
- **UI Configuration**: Dynamic theming data included in responses
- **Mock Data Support**: Local asset data for development and testing
- **Network Simulation**: Realistic network delays and error simulation

### ✅ Error Handling

- **Graceful Recovery**: Comprehensive error handling with fallbacks
- **Type Safety**: Sealed Result<T> class for compile-time safety
- **User-Friendly Messages**: Clear error messages for users
- **Loading States**: Proper loading state management
- **Network Errors**: Specific handling for network-related issues

### ✅ Type Safety

- **Sealed Classes**: Result<T> for type-safe state management
- **Compile-Time Safety**: All operations are type-safe
- **Null Safety**: Proper null handling with defaults
- **Domain Validation**: Domain models ensure data integrity

### ✅ Dynamic UI Configuration

- **Backend-Driven Theming**: UI configuration from backend
- **Color Palettes**: Dynamic color schemes for movies
- **Text Configuration**: Configurable text content
- **Button Styling**: Dynamic button appearance
- **Search Configuration**: Search-specific UI settings

### ✅ Repository Pattern

- **Interface Contracts**: Clean repository interface definitions
- **Implementation Separation**: Clear separation of interface and implementation
- **Dependency Injection**: Koin-based dependency injection
- **Testable Architecture**: Easy to mock and test
- **Build Variant Support**: Different implementations for different build types

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

- **Koin**: Dependency injection framework
- **Coroutines**: Asynchronous programming with suspend functions
- **Gson**: JSON parsing and serialization
- **Jetpack Compose**: UI framework integration with Color objects
- **Ktor**: HTTP client for MCP communication
- **Android Context**: Asset loading and resource access

## Build Status

✅ **BUILD SUCCESSFUL** - All data layer components compile correctly and are fully integrated with the presentation layer.

✅ **MVI MODEL VERIFIED** - Complete compliance with MVI architecture Model component.

✅ **MCP INTEGRATION** - Full MCP backend integration with fallback to mock data.

✅ **TYPE SAFETY** - All operations are type-safe with sealed Result<T> classes.

✅ **ERROR HANDLING** - Comprehensive error handling with user-friendly messages.

✅ **UI CONFIGURATION** - Dynamic theming support from backend.

The data layer is now ready for production use with complete Clean Architecture implementation, MVI pattern support, MCP backend integration, and seamless presentation layer integration.
