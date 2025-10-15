# Navigation Layer

Type-safe navigation layer with device-adaptive patterns for Cinemy Android app.

## Structure

```
navigation/
├── Navigation.kt           # Main navigation composable
├── Screen.kt              # Type-safe route definitions  
└── NavigationConstants.kt # Navigation constants
```

## Navigation Flow

### Single-Pane (Phone)
```
Splash → MoviesList → MovieDetail
```

### Dual-Pane (Tablet/Foldable)
```
Splash → DualPane → DualPaneWithMovie
```

## Quick Start

```kotlin
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Splash.route) {
        composable(Screen.MoviesList.route) {
            MoviesListScreen(
                onMovieClick = { movie ->
                    navController.navigate(Screen.MovieDetail(movie.id).createRoute())
                }
            )
        }
    }
}
```

## Architecture Decisions

### Type-Safe Navigation
- **Why**: Compile-time safety prevents navigation errors
- **Implementation**: Sealed classes with route parameters
- **Benefit**: IDE support and refactoring safety

### Device-Adaptive Navigation  
- **Why**: Different UX patterns for different screen sizes
- **Implementation**: `supportsDualPane()` detection
- **Benefit**: Optimal UX for each device type

### Centralized Constants
- **Why**: Single source of truth for routes and parameters
- **Implementation**: `NavigationConstants` object
- **Benefit**: Easy maintenance and consistency

## Configuration

### Core Files
- `Navigation.kt` - Main navigation composable
- `Screen.kt` - Type-safe route definitions
- `NavigationConstants.kt` - Route constants

### Dependencies
- Jetpack Compose Navigation 2.9.3
- Koin 3.5.3 for ViewModel injection
- DeviceUtils for capability detection

## FAQ

**Q: How to add new route?**
A: Add sealed class to `Screen.kt` and constant to `NavigationConstants.kt`

**Q: How to pass parameters?**
A: Use data class with `createRoute()` method for type-safe parameter passing

**Q: How to handle device differences?**
A: Use `supportsDualPane()` to detect capabilities and route accordingly

**Q: How to integrate ViewModels?**
A: Use `koinViewModel()` in composable destinations for automatic injection
