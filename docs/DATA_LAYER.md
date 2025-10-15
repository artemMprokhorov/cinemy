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

- **Interface Implementation**: Implements `MovieApiService` interface.
- **MCP Integration**: Delegates transport to `McpHttpClient`.
- **Asset Loading**: Uses `AssetDataLoader` to source UI configuration and to compose response `meta`.
- **Array-wrapped Responses**: Tolerates backend responses where payloads are wrapped in a single-element array; the first element is used as the effective payload for list and details flows.
- **Methods**:
  - `getPopularMovies(page: Int): McpResponseDto<MovieListResponseDto>`
    - Sends `getPopularMovies` request with `page`.
    - Maps the first element payload to `MovieListResponseDto`.
    - Attaches `uiConfig` from assets and `meta` with results count.
    - Non-throwing: failures return `success=false`, `data=null`, `error` populated.
  - `getMovieDetails(movieId: Int): McpResponseDto<MovieDetailsDto>`
    - Sends `getMovieDetails` request with `movieId`.
    - Expects map containing `movieDetails` at top level (DTO path).
    - Attaches `uiConfig` from assets and `meta` with `movieId` context.
    - Non-throwing: failures return `success=false`, `data=null`, `error` populated.
  - `getPopularMoviesViaMcp(page: Int): Result<MovieListResponse>`
    - Same transport as above but maps to domain `MovieListResponse` and domain `UiConfiguration`.
    - Non-throwing: uses `runCatching`; failures returned as `Result.Error`.
  - `getMovieDetailsViaMcp(movieId: Int): Result<MovieDetailsResponse>`
    - Accepts array-wrapped response; reads nested `data["data"]["movieDetails"]` per backend contract.
    - Extracts backend `uiConfig` when present and maps to domain.
    - Composes domain `MovieDetailsResponse` with `Meta` (including `version`, `geminiColors`, and `movieRating`).
    - Non-throwing: uses `runCatching`; failures returned as `Result.Error`.
- **Response Processing**: Converts MCP responses to DTOs and domain models using `MovieMapper`.
- **Error Handling**: Non-throwing public API; consistent error representation via `McpResponseDto` and `Result`.

#### Supporting MCP Components

- **AssetDataLoader** (`AssetDataLoader.kt`) - Loads UI configuration, metadata, and mock movies from assets with resilient fallbacks.
  
  - Purpose: Synchronous, non-throwing asset reader producing DTOs used upstream by mappers and repositories.
  - Behavior: All public methods guard execution with `runCatching { ... }` and return defaults/empty lists on any error or missing sections.
  - Methods:
    - `loadUiConfig(): UiConfigurationDto`
      - Reads top-level `uiConfig` with nested `colors`, `texts`, `buttons`; returns default configuration if unavailable.
      - Never throws; returns defaults on error.
    - `loadMetaData(method: String, resultsCount: Int = 0, movieId: Int? = null): MetaDto`
      - Reads top-level `meta`; composes metadata using provided context params when present, otherwise returns default metadata based on inputs.
      - Never throws; returns defaults on error.
    - `loadMockMovies(): List<MovieDto>`
      - New contract: attempts to parse the asset as a JSON array and extract `serializedResults` from the first object.
      - Legacy contract: falls back to object structure `data.movies` or `data.serializedResults`.
      - Never throws; returns empty list on error/missing data.
  - Notes: Threading left to callers; functions are CPU-light JSON parsing and synchronous.
- **McpHttpClient** (`McpHttpClient.kt`)
  - Purpose: HTTP transport for MCP communication using Ktor with mock fallback.
  - Routing:
    - When `BuildConfig.USE_MOCK_DATA == true` → handled entirely by `FakeInterceptor` (assets-based, no network).
    - When `BuildConfig.USE_MOCK_DATA == false` and `BuildConfig.MCP_SERVER_URL` is blank → transparently falls back to `FakeInterceptor`.
    - Otherwise → posts JSON to `BuildConfig.MCP_SERVER_URL` with `Content-Type: application/json`.
  - Request Building: Uses `HttpRequestMapper.buildJsonRequestBody(request)` to serialize `McpRequest` to JSON.
  - Response Validation: Rejects HTML/error pages and non-JSON payloads before parsing.
  - Parsing Strategy:
    - Array-first: attempts `parseJsonArrayResponse<T>(JSONArray(response))` to support array-wrapped payloads like `[{...}]`.
    - Fallback: `parseJsonStringResponse<T>(response)` for direct object or raw-string cases.
  - Error Semantics: Non-throwing public API; failures return `McpResponse(success=false, data=null, error=HTTP_ERROR_NETWORK_ERROR.format(message), message=HTTP_ERROR_UNABLE_TO_CONNECT)`.
  - Resource Management: `close()` closes the underlying Ktor `HttpClient`.
- **FakeInterceptor** (`FakeInterceptor.kt`) - Network interceptor for testing and development

#### FakeInterceptor (`FakeInterceptor.kt`)

- **Purpose**: Provides mock responses from asset files for development and testing, simulating realistic network behavior with delays and error scenarios.
- **Behavior**: All public methods guard execution with `runCatching { ... }` and return fallback responses on any error or missing asset files.
- **Network Simulation**: Adds realistic random delays to simulate network latency for testing purposes.
- **Methods**:
  - `intercept<T>(request: McpRequest): McpResponse<T>`
    - Main public method that intercepts MCP requests and returns mock responses
    - Supports two operations: `getPopularMovies` with pagination and `getMovieDetails`
    - Simulates network delay using configurable base and random delay constants
    - Returns appropriate mock data or error responses based on request method
    - Never throws exceptions; errors are returned as failed McpResponse
  - `loadMockDataFromAssetsWithPagination(fileName: String, page: Int): McpResponse<Any>`
    - Loads and parses JSON data from assets with pagination support
    - Slices data based on requested page (15 movies per page, 3 total pages)
    - Creates pagination metadata including totalPages, hasNext, hasPrevious flags
    - Returns structured response with movies and pagination information
  - `loadMockDataFromAssets(fileName: String): McpResponse<Any>`
    - Loads JSON data from assets for single item responses (e.g., movie details)
    - Returns data as-is without pagination processing
    - Used for non-paginated responses like individual movie details
  - `parseJsonResponse(jsonString: String): Map<String, Any>`
    - Converts JSON string to native Kotlin Map structure
    - Used internally for parsing asset JSON data
  - `jsonObjectToMap(jsonObject: JSONObject): Map<String, Any>`
    - Recursively converts JSONObject to Map, handling nested objects and arrays
    - Used internally for JSON parsing operations
  - `jsonArrayToList(jsonArray: JSONArray): List<Any>`
    - Recursively converts JSONArray to List, handling nested structures
    - Used internally for JSON parsing operations
  - `createFallbackResponse(): McpResponse<Any>`
    - Creates standardized error response when asset loading fails
    - Provides consistent error handling across the interceptor
- **Asset Files**: Uses `ASSET_MOCK_MOVIES` and `ASSET_MOCK_MOVIE_DETAILS` for mock data
- **Error Handling**: Comprehensive error handling with fallback responses for missing or corrupted asset files
- **Documentation Quality**: ✅ **FULLY DOCUMENTED** - Complete KDoc with parameter descriptions, return types, and cross-references
- **MCP Models** (`models/`):
  - `McpRequest.kt` - MCP request structure with comprehensive documentation
  - `McpResponse.kt` - Generic MCP response wrapper with success/data/error/message contract

#### MCP Request Model (`McpRequest.kt`)

- **Purpose**: Represents an MCP (Model Context Protocol) request structure for backend communication
- **Documentation**: Comprehensive KDoc documentation with detailed parameter descriptions
- **Structure**: 
  - `method: String` - The method name to be called on the MCP server (e.g., "getPopularMovies", "getMovieDetails")
  - `params: Map<String, String>` - Parameters as key-value pairs, defaults to empty map
- **Features**:
  - Type-safe request structure for MCP communication
  - Default parameter handling for methods without parameters
  - Full KDoc documentation with cross-references to related components
  - Integration with HttpRequestMapper for JSON serialization
  - Support for all MCP server methods used in the application
- **Usage**: Used by McpClient and HttpRequestMapper for MCP backend communication
- **Documentation Quality**: ✅ **FULLY DOCUMENTED** - Complete KDoc with parameter descriptions, usage examples, and cross-references

#### MCP Response Model (`McpResponse.kt`)

- **Purpose**: Generic wrapper representing the outcome of MCP operations.
- **Structure**:
  - `success: Boolean` — Indicates whether the operation completed successfully.
  - `data: T?` — Optional payload (present on success; generic type `T`).
  - `error: String?` — Optional human-readable error description (present on failure).
  - `message: String` — Optional diagnostic/info message (e.g., normalized success markers or raw-response notes).
- **Contract**:
  - If `success == true`: `data` SHOULD be non-null for payload-bearing operations, `error` SHOULD be null.
  - If `success == false`: `data` SHOULD be null, `error` SHOULD describe the failure.
  - `message` may contain auxiliary information and is safe for debugging output.
- **Exceptions**: The model does not throw; error semantics are expressed via fields.
- **Integration**: Produced by `HttpResponseMapper` when parsing HTTP/MCP responses and consumed across repositories/data sources.
- **Generics**: Type parameter `T` denotes the expected payload type when successful.
- **Documentation Quality**: ✅ **FULLY DOCUMENTED** — Complete KDoc with field-level semantics and usage notes.

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

#### Default Seeds and Converters (`DefaultData.kt`)

- **Purpose**: Provide sensible, strongly-typed defaults for demos/tests and convert them into domain models.
- **Classes**:
  - `DefaultMovieDetails`
    - Fields: mirrors core `MovieDetails` attributes such as identifiers, title, description, poster/backdrop paths, rating, vote count, release date, runtime, genres, production companies, budget, revenue, status.
    - Method: `toMovieDetails()` → returns domain `MovieDetails`.
      - Non-throwing; directly maps each property to the domain model.
  - `DefaultUiConfiguration`
    - Fields: Compose `Color` palette values, app texts (title, loading/error/no-movies, buttons), and button settings (colors, text color, corner radius).
    - Method: `toUiConfiguration()` → returns domain `UiConfiguration`.
      - Non-throwing; builds `ColorScheme`, `TextConfiguration`, `ButtonConfiguration`.
  - `DefaultMeta`
    - Fields: timestamp, MCP `method`, `resultsCount`, `aiGenerated` flag, color hexes, and `version`.
    - Method: `toMeta()` → returns domain `Meta` with `GeminiColors` composed from the seed.
      - Non-throwing; composes `Meta` verbatim from provided defaults.

Documentation quality: the three converter methods are fully documented in KDoc with purpose, return type, and exception semantics.

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

### 5. HTTP Mappers (`mapper/`)

#### HttpRequestMapper (`HttpRequestMapper.kt`)

- **Purpose**: Handles HTTP request building and JSON serialization for MCP communication
- **JSON Generation**: Converts MCP requests to properly formatted JSON strings
- **Security**: Implements JSON string escaping to prevent injection attacks
- **Type Safety**: Handles different data types (String, Number, Boolean, Map, List)

#### Key Methods

- **`buildJsonRequestBody(request: McpRequest)`** - Main method for creating JSON request bodies
  - Validates request parameters before processing
  - Returns JSON string in format: `{"method":"methodName","params":{"key":"value"}}`
  - Throws `IllegalArgumentException` for invalid requests

- **`validateRequest(request: McpRequest)`** - Request validation
  - Checks for non-empty method and at least one parameter
  - Returns boolean validation result

- **`escapeJsonString(value: String)`** - JSON string escaping
  - Applies proper escape sequences for backslash, quotes, and control characters
  - Prevents malformed JSON and security issues

- **`parameterValueToJsonString(value: Any)`** - Type-safe JSON conversion
  - Handles String, Number, Boolean, Map, and List types
  - Recursively processes complex data structures

- **`formatParameterEntry(key: String, value: Any)`** - Parameter formatting
  - Creates formatted JSON parameter entries: `"key":"value"`

#### Private Helper Methods

- **`buildJsonFromMap(map: Map<String, Any>)`** - Recursive map to JSON conversion
- **`buildJsonFromList(list: List<*>)`** - Recursive list to JSON array conversion

#### Features

- **JSON Security**: Comprehensive string escaping to prevent injection
- **Type Support**: Full support for primitive and complex data types
- **Recursive Processing**: Handles nested Maps and Lists
- **Error Handling**: Validation with meaningful error messages
- **Performance**: Efficient string building with StringBuilder

#### HttpResponseMapper (`HttpResponseMapper.kt`)

- **Purpose**: Parses HTTP/MCP JSON responses into native Kotlin structures and wraps them in `McpResponse<T>` when needed.
- **JSON Parsing**: Converts `JSONObject`/`JSONArray`/raw JSON strings to `Map<String, Any>` and `List<Any>` recursively.
- **Robustness**: For string responses, on parse failure returns a successful response with the raw string and `MCP_MESSAGE_REAL_REQUEST_RAW_RESPONSE`.

#### Key Methods

- **`parseJsonArrayResponse(jsonArray: JSONArray): McpResponse<T>`**
  - Parses the first `JSONObject` into `Map<String, Any>` and returns `McpResponse(success=true, message=MCP_MESSAGE_REAL_REQUEST_SUCCESSFUL)`.
  - Throws `Exception` if the array is empty.

- **`parseJsonStringResponse(jsonString: String): McpResponse<T>`**
  - On success: `data` is the parsed map, `message=MCP_MESSAGE_REAL_REQUEST_SUCCESSFUL`.
  - On parse failure: `data` is the raw string, `message=MCP_MESSAGE_REAL_REQUEST_RAW_RESPONSE`.

- **`parseJsonObject(jsonObject: JSONObject): Map<String, Any>`**, **`parseJsonResponse(jsonString: String): Map<String, Any>`**
  - Recursive conversions to native maps/lists.

- **`jsonObjectToMap(jsonObject: JSONObject): Map<String, Any>`**, **`jsonArrayToList(jsonArray: JSONArray): List<Any>`**, **`parseValue(value: Any): Any`**
  - Utilities used by higher-level parsers; handle nested structures consistently.

#### Behavior and Error Handling

- **Success Messages**: `MCP_MESSAGE_REAL_REQUEST_SUCCESSFUL` for parsed responses; `MCP_MESSAGE_REAL_REQUEST_RAW_RESPONSE` when returning raw string on parse failure.
- **Exceptions**: `parseJsonArrayResponse` throws on empty arrays; `org.json` operations may throw `org.json.JSONException`.
- **Type Casting**: `McpResponse<T>.data` is assigned from parsed maps or raw strings; callers must request the correct `T`.
 - **Contract Alignment**: When a parse operation succeeds, `success=true`, `error=null`, and `data` carries the payload. On explicit failure paths in callers, prefer `success=false` with `error` populated per `McpResponse` contract.

### 6. Repository Layer (`repository/`)

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

### 7. Utility Classes (`util/`)

#### AssetUtils (`AssetUtils.kt`)

- **Asset Loading**: Utility functions for loading JSON data from assets
- **File Operations**: Helper methods for asset file management
- **Error Handling**: Safe asset loading with fallback mechanisms

#### ColorUtils (`ColorUtils.kt`)

- **Color Conversion**: Converts string colors to Compose Color objects
- **Color Parsing**: Handles hex color string parsing
- **Default Colors**: Provides fallback colors for invalid color strings
- **Color Validation**: Ensures color strings are valid before conversion

### 8. Dependency Injection (`di/DataModule.kt`)

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
// Clean domain models for UI consumption (exact signature from model/Movie.kt)
data class Movie(
    val id: Int,
    val title: String,
    val description: String,
    val posterPath: String?,
    val backdropPath: String?,
    val rating: Double,
    val voteCount: Int,
    val releaseDate: String,
    val genreIds: List<Int> = emptyList(),
    val popularity: Double,
    val adult: Boolean,
    val originalLanguage: String,
    val originalTitle: String,
    val video: Boolean,
    val colors: MovieColors
)

// Result handling for type-safe state management (exact signature)
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
