# TMDB AI Android Application

A modern Android application built with Jetpack Compose, following the MVI (Model-View-Intent) architecture pattern, Clean Architecture principles, and featuring dynamic UI configuration from backend responses.

## 🏗️ Architecture Overview

This project implements a complete **MVI (Model-View-Intent) architecture pattern** with the following structure:

```
app/src/main/java/com/example/tmdbai/
├── data/                     # MVI Model Layer
│   ├── model/               # Domain models and Result types
│   ├── repository/          # Repository interfaces and implementations
│   ├── remote/              # API services and DTOs
│   ├── mapper/              # Data transformation logic
│   ├── mcp/                 # Backend communication client
│   └── di/                  # Dependency injection modules
├── presentation/            # MVI Intent & State Layer
│   ├── commons/             # Common base classes
│   ├── movieslist/          # Movies list screen logic
│   ├── moviedetail/         # Movie details screen logic
│   └── di/                  # Presentation DI modules
├── ui/                      # MVI View Layer
│   ├── movieslist/          # Movies list UI components
│   ├── moviedetail/         # Movie details UI components
│   ├── splash/              # Splash screen UI
│   └── theme/               # UI theming and styling
├── navigation/              # Navigation components
└── utils/                   # Utility classes
```

## 🎯 MVI Architecture Pattern Implementation

### ✅ **Complete MVI Pattern Compliance**

This application **fully implements the MVI (Model-View-Intent) architecture pattern**:

#### **1. MODEL (Data Layer)**
- **Domain Models**: Clean data models (`Movie`, `MovieDetails`, `Genre`, etc.)
- **Repository Pattern**: `MovieRepository` interface with implementation
- **Result Handling**: Sealed `Result<T>` class for type-safe state management
- **Business Logic**: Encapsulated in repository layer
- **Data Sources**: MCP client for backend communication

#### **2. VIEW (UI Layer)**
- **Jetpack Compose**: Modern declarative UI components
- **State-Driven**: UI reacts to state changes automatically
- **Intent Sending**: UI sends intents to ViewModels
- **Separation of Concerns**: UI only handles display logic
- **Dynamic Theming**: Backend-driven UI configuration

#### **3. INTENT (User Interactions)**
- **Sealed Classes**: Type-safe intent definitions
- **Comprehensive Coverage**: All user interactions captured
- **Interface Inheritance**: Proper `CommonIntent` interface structure
- **Examples**: `LoadPopularMovies`, `SearchMovies`, `Retry`, `Refresh`

#### **4. STATE (UI State Management)**
- **Immutable State**: Data classes with default values
- **StateFlow**: Reactive state management
- **Comprehensive State**: All UI state in single object
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

| MVI Component | Status | Implementation |
|---------------|--------|----------------|
| **Model** | ✅ | Data layer with repositories and business logic |
| **View** | ✅ | Jetpack Compose UI components |
| **Intent** | ✅ | Sealed classes for user interactions |
| **State** | ✅ | Immutable data classes with StateFlow |
| **Unidirectional Flow** | ✅ | Intent → Reducer → State → UI |
| **State Management** | ✅ | StateFlow for reactive updates |
| **Separation of Concerns** | ✅ | Clear boundaries between layers |
| **Type Safety** | ✅ | Sealed classes and Result types |
| **Error Handling** | ✅ | Result sealed class with error states |
| **Reactive UI** | ✅ | Automatic UI updates via StateFlow |

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

### ✅ **Dynamic UI Configuration**
- Backend-driven theming
- Configurable colors and texts
- Dynamic button styling
- Movie poster color palettes

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
- Dynamic theming
- Error handling with retry

### **Movie Details Screen**
- Comprehensive movie information
- Rich content display
- Dynamic theming
- Back navigation

## 🛠️ Technical Stack

### **Architecture & Patterns**
- **MVI (Model-View-Intent)**: Primary architecture pattern
- **Clean Architecture**: Separation of concerns
- **Repository Pattern**: Data access abstraction
- **Dependency Injection**: Koin framework

### **UI Framework**
- **Jetpack Compose**: Modern declarative UI
- **Material Design 3**: Design system
- **Navigation Compose**: Screen navigation
- **StateFlow**: Reactive state management

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
- **MCP Client**: Backend communication
- **DTOs**: Data transfer objects
- **Domain Models**: Clean business models

## 🔧 Setup and Installation

### **Prerequisites**
- Android Studio Arctic Fox or later
- Kotlin 1.8+
- Android SDK 34+
- Gradle 8.0+

### **Build Configuration**
```kotlin
// app/build.gradle.kts
android {
    compileSdk = 34
    defaultConfig {
        minSdk = 24
        targetSdk = 34
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}
```

### **Dependencies**
```kotlin
dependencies {
    // Compose
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.4")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.5")
    
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    
    // Koin
    implementation("io.insert-koin:koin-android:3.5.0")
    implementation("io.insert-koin:koin-androidx-compose:3.5.0")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    
    // JSON Parsing
    implementation("com.google.code.gson:gson:2.10.1")
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

✅ **ARCHITECTURE VALIDATED** - Clean Architecture and MVI pattern properly implemented.

## 📚 Documentation

- **[Data Layer Documentation](app/src/main/java/com/example/tmdbai/data/README.md)** - MVI Model implementation
- **[Presentation Layer Documentation](app/src/main/java/com/example/tmdbai/presentation/README.md)** - MVI Intent & State implementation
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

This application demonstrates a **production-ready implementation** of the MVI architecture pattern with:

- ✅ **Complete MVI Compliance**: All components properly implemented
- ✅ **Modern Android Development**: Latest tools and frameworks
- ✅ **Clean Architecture**: Proper separation of concerns
- ✅ **Type Safety**: Sealed classes and Result types
- ✅ **Reactive UI**: StateFlow for automatic updates
- ✅ **Dynamic Theming**: Backend-driven UI configuration

The application is ready for production use and serves as an excellent example of MVI architecture implementation in Android development! 🚀✨
