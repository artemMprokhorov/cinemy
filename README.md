# TMDB AI Android Application

A modern Android application built with Jetpack Compose, following the MVI (Model-View-Intent) architecture pattern, Clean Architecture principles, and featuring **server-driven UI** with **MCP (Model Context Protocol)** backend integration.

## 🏗️ Architecture Overview

This project implements a complete **MVI (Model-View-Intent) architecture pattern** with **MCP backend integration** and **server-driven UI**:

```
app/src/main/java/com/example/tmdbai/
├── data/                     # MVI Model Layer
│   ├── model/               # Domain models and Result types
│   ├── repository/          # Repository interfaces and implementations
│   ├── remote/              # API services and DTOs
│   ├── mapper/              # Data transformation logic
│   ├── mcp/                 # 🆕 MCP protocol implementation
│   │   ├── models/          # MCP request/response models
│   │   ├── McpHttpClient.kt # Ktor-based HTTP client
│   │   └── McpClient.kt     # Business logic client
│   └── di/                  # Dependency injection modules
├── presentation/            # MVI Intent & State Layer
│   ├── commons/             # Common base classes
│   ├── movieslist/          # Movies list screen logic
│   ├── moviedetail/         # Movie details screen logic
│   └── di/                  # Presentation DI modules
├── ui/                      # MVI View Layer
│   ├── components/          # 🆕 Server-driven UI components
│   │   ├── ConfigurableButton.kt    # Dynamic button styling
│   │   ├── ConfigurableText.kt      # Dynamic text styling
│   │   └── ConfigurableMovieCard.kt # Dynamic movie card theming
│   ├── movieslist/          # Movies list UI components
│   ├── moviedetail/         # Movie details UI components
│   ├── splash/              # Splash screen UI
│   └── theme/               # UI theming and styling
├── navigation/              # Type-safe navigation components
└── utils/                   # Utility classes
```

## 🎯 MVI Architecture Pattern Implementation

### ✅ **Complete MVI Pattern Compliance**

This application **fully implements the MVI (Model-View-Intent) architecture pattern** with **MCP backend integration**:

#### **1. MODEL (Data Layer)**
- **Domain Models**: Clean data models (`Movie`, `MovieDetails`, `Genre`, etc.)
- **Repository Pattern**: `MovieRepository` interface with implementation
- **Result Handling**: Sealed `Result<T>` class for type-safe state management
- **Business Logic**: Encapsulated in repository layer
- **MCP Integration**: 🆕 **Model Context Protocol** for backend communication
- **Server-Driven Data**: Dynamic UI configuration from backend responses

#### **2. VIEW (UI Layer)**
- **Jetpack Compose**: Modern declarative UI components
- **State-Driven**: UI reacts to state changes automatically
- **Intent Sending**: UI sends intents to ViewModels
- **Separation of Concerns**: UI only handles display logic
- **Server-Driven UI**: 🆕 **Dynamic theming** from MCP backend responses
- **Configurable Components**: 🆕 **ConfigurableButton**, **ConfigurableText**, **ConfigurableMovieCard**

#### **3. INTENT (User Interactions)**
- **Sealed Classes**: Type-safe intent definitions
- **Comprehensive Coverage**: All user interactions captured
- **Interface Inheritance**: Proper `CommonIntent` interface structure
- **Examples**: `LoadPopularMovies`, `SearchMovies`, `Retry`, `Refresh`

#### **4. STATE (UI State Management)**
- **Immutable State**: Data classes with default values
- **StateFlow**: Reactive state management
- **Comprehensive State**: All UI state in single object
- **UI Configuration**: 🆕 **Dynamic theming** support via `UiConfiguration`

#### **5. VIEWMODEL (Business Logic)**
- **Intent Processing**: `processIntent()` method
- **State Management**: `StateFlow` for reactive updates
- **Unidirectional Flow**: Intent → Reducer → New State
- **Repository Integration**: Clean data layer access
- **MCP Integration**: 🆕 **Server-driven** business logic

### 🔄 **MVI Data Flow with MCP Integration**

#### **Unidirectional Data Flow:**
```
User Action → Intent → ViewModel.processIntent() → Repository → MCP Client → Backend → State Update → UI Update
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

// 3. Repository calls MCP client
val result = movieRepository.getPopularMovies(page)

// 4. MCP client communicates with backend
val mcpResponse = mcpClient.getPopularMoviesViaMcp()

// 5. State update with server-driven UI config
_state.value = _state.value.copy(
    movies = result.data.movies,
    isLoading = false,
    uiConfig = result.uiConfig  // 🆕 Server-driven UI configuration
)

// 6. UI automatically updates via StateFlow with dynamic theming
val state by viewModel.state.collectAsState()
```

### ✅ **MVI Pattern Compliance Checklist**

| MVI Component | Status | Implementation |
|---------------|--------|----------------|
| **Model** | ✅ | Data layer with repositories and MCP integration |
| **View** | ✅ | Jetpack Compose with server-driven UI components |
| **Intent** | ✅ | Sealed classes for user interactions |
| **State** | ✅ | Immutable data classes with StateFlow and UiConfig |
| **Unidirectional Flow** | ✅ | Intent → Reducer → State → UI |
| **State Management** | ✅ | StateFlow for reactive updates |
| **Separation of Concerns** | ✅ | Clear boundaries between layers |
| **Type Safety** | ✅ | Sealed classes and Result types |
| **Error Handling** | ✅ | Result sealed class with error states |
| **Reactive UI** | ✅ | Automatic UI updates via StateFlow |
| **MCP Integration** | ✅ | Backend communication via Model Context Protocol |
| **Server-Driven UI** | ✅ | Dynamic theming from backend responses |

## 🚀 Key Features

### ✅ **MVI Architecture**
- Complete MVI pattern implementation
- Unidirectional data flow
- Intent-based user interactions
- Reactive state management

### ✅ **Modern Android Development**
- **Jetpack Compose**: Modern declarative UI
- **Kotlin Coroutines**: Asynchronous programming
- **StateFlow**: Reactive state management
- **Koin**: Dependency injection
- **Navigation Compose**: Type-safe navigation

### ✅ **Clean Architecture**
- Clear separation of concerns
- Repository pattern
- Dependency inversion
- Testable architecture

### ✅ **🆕 MCP (Model Context Protocol) Integration**
- **Backend Communication**: All data via MCP protocol
- **AI-Powered Responses**: Dynamic content generation
- **Server-Driven Logic**: Business logic from backend
- **JSON Protocol**: Standardized communication format

### ✅ **🆕 Server-Driven UI System**
- **Dynamic Theming**: Colors, fonts, and styles from backend
- **Configurable Components**: Button, Text, and Card components
- **Real-time Updates**: UI changes without app updates
- **AI-Generated Styling**: Intelligent color schemes and layouts

### ✅ **Dynamic UI Configuration**
- Backend-driven theming
- Configurable colors and texts
- Dynamic button styling
- Movie poster color palettes
- **🆕 Server-controlled** appearance

### ✅ **Error Handling**
- Graceful error recovery
- Retry functionality
- User-friendly error messages
- Loading state management

### ✅ **Search and Pagination**
- Movie search functionality
- Pagination controls
- Search mode toggle
- Query persistence

## 📱 Screens

### **Splash Screen**
- App branding and loading
- Automatic navigation to movies list

### **Movies List Screen**
- Grid of movie posters
- Search functionality
- Pagination controls
- **🆕 Server-driven theming**
- **🆕 ConfigurableMovieCard** components
- Error handling with retry

### **Movie Details Screen**
- Comprehensive movie information
- Rich content display
- **🆕 Server-driven theming**
- **🆕 ConfigurableText** components
- Back navigation

## 🛠️ Technical Stack

### **Architecture & Patterns**
- **MVI (Model-View-Intent)**: Primary architecture pattern
- **Clean Architecture**: Separation of concerns
- **Repository Pattern**: Data access abstraction
- **Dependency Injection**: Koin framework
- **🆕 MCP Protocol**: Model Context Protocol for backend communication

### **UI Framework**
- **Jetpack Compose**: Modern declarative UI
- **Material Design 3**: Design system
- **Navigation Compose**: Type-safe screen navigation
- **StateFlow**: Reactive state management
- **🆕 Server-Driven UI**: Dynamic theming system

### **Asynchronous Programming**
- **Kotlin Coroutines**: Asynchronous operations
- **StateFlow**: Reactive streams
- **Suspend Functions**: Non-blocking operations

### **Dependency Injection**
- **Koin**: Lightweight DI framework
- **ViewModel Injection**: Automatic ViewModel creation
- **Repository Injection**: Clean dependency management

### **Data Layer**
- **Repository Pattern**: Data access abstraction
- **🆕 MCP Client**: Backend communication via Model Context Protocol
- **DTOs**: Data transfer objects
- **Domain Models**: Clean business models
- **🆕 Server-Driven Data**: Dynamic UI configuration

### **🆕 MCP Integration**
- **Ktor HTTP Client**: Modern HTTP client for MCP communication
- **JSON Serialization**: Gson for MCP request/response handling
- **Error Handling**: Result sealed class for MCP responses
- **Configuration**: BuildConfig-based MCP server URL

## 🔧 Setup and Installation

### **Prerequisites**
- Android Studio Arctic Fox or later
- Kotlin 1.9.22 (stable version)
- Android SDK 36
- Gradle 8.10.2

### **Build Configuration**
```kotlin
// app/build.gradle.kts
android {
    compileSdk = 36
    defaultConfig {
        minSdk = 24
        targetSdk = 36
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}
```

### **Dependencies**
```kotlin
dependencies {
    // Compose
    implementation(platform("androidx.compose:compose-bom:2025.01.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.2")
    
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    
    // Koin
    implementation("io.insert-koin:koin-android:3.5.3")
    implementation("io.insert-koin:koin-androidx-compose:3.5.3")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    
    // 🆕 MCP Integration
    implementation("io.ktor:ktor-client-core:2.1.3")
    implementation("io.ktor:ktor-client-android:2.1.3")
    implementation("io.ktor:ktor-client-content-negotiation:2.1.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.1.3")
    implementation("io.ktor:ktor-client-logging:2.1.3")
    
    // JSON Parsing
    implementation("com.google.code.gson:gson:2.10.1")
    
    // Image Loading
    implementation("io.coil-kt:coil-compose:2.5.0")
}
```

## 🏃‍♂️ Running the Application

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd TmdbAi
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the project directory

3. **Build and Run**
   - Connect an Android device or start an emulator
   - Click "Run" or press Shift+F10
   - The app will install and launch automatically

## 📊 Build Status

✅ **BUILD SUCCESSFUL** - All components compile correctly and are fully integrated.

✅ **MVI PATTERN VERIFIED** - Complete compliance with MVI architecture principles.

✅ **MCP INTEGRATION COMPLETE** - Model Context Protocol fully implemented.

✅ **SERVER-DRIVEN UI ACTIVE** - Dynamic theming system operational.

✅ **ARCHITECTURE VALIDATED** - Clean Architecture and MVI pattern properly implemented.

## 📚 Documentation

- **[Data Layer Documentation](app/src/main/java/com/example/tmdbai/data/README.md)** - MVI Model implementation with MCP
- **[Presentation Layer Documentation](app/src/main/java/com/example/tmdbai/presentation/README.md)** - MVI Intent & State implementation
- **[UI Components Documentation](app/src/main/java/com/example/tmdbai/ui/components/README.md)** - Server-driven UI components
- **[MCP Integration Guide](app/src/main/java/com/example/tmdbai/data/mcp/README.md)** - Model Context Protocol implementation
- **[Utils Documentation](app/src/main/java/com/example/tmdbai/utils/README.md)** - Utility classes

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes following MVI architecture principles
4. Ensure all tests pass
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🎉 Conclusion

This application demonstrates a **production-ready implementation** of the MVI architecture pattern with **MCP backend integration** and **server-driven UI**:

- ✅ **Complete MVI Compliance**: All components properly implemented
- ✅ **Modern Android Development**: Latest tools and frameworks
- ✅ **Clean Architecture**: Proper separation of concerns
- ✅ **Type Safety**: Sealed classes and Result types
- ✅ **Reactive UI**: StateFlow for automatic updates
- ✅ **🆕 MCP Integration**: Model Context Protocol for backend communication
- ✅ **🆕 Server-Driven UI**: Dynamic theming from backend responses
- ✅ **🆕 AI-Powered**: Backend-driven content and styling

The application is ready for production use and serves as an excellent example of **MVI architecture with MCP integration** in Android development! 🚀✨

## 🔄 Recent Updates

### **Latest Features Added:**
- ✅ **MCP Protocol Integration** - Complete backend communication system
- ✅ **Server-Driven UI Components** - Dynamic theming system
- ✅ **Configurable Components** - Button, Text, and MovieCard with backend styling
- ✅ **Type-Safe Navigation** - Sealed class-based navigation routes
- ✅ **MCP HTTP Client** - Ktor-based communication layer
- ✅ **Business Logic Integration** - Repository layer using MCP client
- ✅ **Import Optimization** - Cleaned unused imports for better performance

### **Current Status:**
- 🚀 **App Successfully Built** - All components integrated
- 🚀 **App Successfully Installed** - Running on device
- 🚀 **Server-Driven UI Active** - Dynamic theming operational
- 🚀 **MCP Integration Complete** - Backend communication ready
