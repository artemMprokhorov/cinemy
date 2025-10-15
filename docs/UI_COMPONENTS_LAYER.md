# UI Components Layer

Server-driven UI components with adaptive layouts and comprehensive theming for Cinemy Android app.

## Structure

```
ui/
├── components/                   # Reusable UI components
│   ├── AdaptiveLayout.kt         # Adaptive layout for foldable devices
│   ├── ConfigurableMovieCard.kt  # Server-driven movie card component
│   ├── ConfigurableText.kt       # Server-driven text component
│   ├── PullToReloadIndicator.kt  # Pull-to-reload arrow indicator
│   ├── SentimentAnalysisCard.kt   # Sentiment analysis display card
│   └── TestUtils.kt              # QA testing utilities
├── dualpane/                     # Dual pane layout components
│   └── DualPaneScreen.kt         # Dual pane screen implementation
├── moviedetail/                  # Movie details screen
│   └── MovieDetailScreen.kt      # Enhanced movie details with complete data
├── movieslist/                   # Movies list screen
│   └── MoviesListScreen.kt       # Movies list with pagination
├── splash/                       # Splash screen
│   └── Splash.kt                 # Application splash screen
└── theme/                        # UI theming system
    ├── Color.kt                  # Color definitions
    ├── Dimens.kt                 # Dimension constants
    ├── Floats.kt                 # Float values
    ├── ImageConfig.kt            # Image configuration
    ├── Theme.kt                  # Material3 theme configuration
    ├── Type.kt                   # Typography types
    └── Typography.kt             # Typography definitions
```

## Data Flow

```
Backend Response → McpClient → MovieMapper → ViewModel → UI Components
     ↓                ↓           ↓           ↓            ↓
  uiConfig        ColorScheme   UiConfig   State      Dynamic Colors
  (JSON)          (DTO)         (Domain)   (UI)       (Applied)
```

## Quick Start

```kotlin
@Composable
fun MoviesListScreen(
    viewModel: MoviesListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    LazyColumn {
        items(state.movies) { movie ->
            ConfigurableMovieCard(
                movie = movie,
                uiConfig = movie.uiConfig,
                onClick = { viewModel.processIntent(MoviesListIntent.NavigateToDetail(movie.id)) }
            )
        }
    }
}
```

## Key Components

### AdaptiveLayout
- Role: Adaptive layout component for foldable devices
- File: `app/src/main/java/org/studioapp/cinemy/ui/components/AdaptiveLayout.kt`
- API: See KDoc in code

### ConfigurableText
- Role: Server-driven text component with dynamic theming
- File: `app/src/main/java/org/studioapp/cinemy/ui/components/ConfigurableText.kt`
- API: See KDoc in code

### ConfigurableMovieCard
- Role: Server-driven movie card with complete information display
- File: `app/src/main/java/org/studioapp/cinemy/ui/components/ConfigurableMovieCard.kt`
- API: See KDoc in code

### SentimentAnalysisCard
- Role: Sentiment analysis results display with review categorization
- File: `app/src/main/java/org/studioapp/cinemy/ui/components/SentimentAnalysisCard.kt`
- API: See KDoc in code

### PullToReloadIndicator
- Role: Pull-to-reload arrow indicator with visual feedback
- File: `app/src/main/java/org/studioapp/cinemy/ui/components/PullToReloadIndicator.kt`
- API: See KDoc in code

### MoviesListScreen
- Role: Movies list with pagination and pull-to-refresh
- File: `app/src/main/java/org/studioapp/cinemy/ui/movieslist/MoviesListScreen.kt`
- API: See KDoc in code

### MovieDetailScreen
- Role: Enhanced movie details with sentiment analysis
- File: `app/src/main/java/org/studioapp/cinemy/ui/moviedetail/MovieDetailScreen.kt`
- API: See KDoc in code

### DualPaneScreen
- Role: Dual pane layout for tablets and foldable devices
- File: `app/src/main/java/org/studioapp/cinemy/ui/dualpane/DualPaneScreen.kt`
- API: See KDoc in code

### Splash
- Role: Application splash screen with branding
- File: `app/src/main/java/org/studioapp/cinemy/ui/splash/Splash.kt`
- API: See KDoc in code

### TestUtils
- Role: QA testing utilities with modifier extensions
- File: `app/src/main/java/org/studioapp/cinemy/ui/components/TestUtils.kt`
- API: See KDoc in code

## Architecture Decisions

### Server-Driven UI
- **Dynamic Colors**: Real-time color theming from backend uiConfig
- **Fallback Strategy**: Graceful fallback to Material3 defaults
- **Type Safety**: Type-safe configuration handling

### MVI Pattern
- **StateFlow**: Reactive state management
- **Intent Processing**: Clean intent handling
- **Reactive Updates**: UI updates automatically on state changes

### Adaptive Layouts
- **Foldable Support**: Automatic layout adaptation
- **Tablet Optimization**: Dual pane for larger screens
- **Responsive Design**: Screen size-aware components

### Accessibility
- **Screen Reader**: TalkBack and VoiceOver support
- **Semantic Roles**: Proper semantic roles for all elements
- **Content Descriptions**: Dynamic descriptions for all states

## Configuration

### Theme Files
- `ui/theme/Color.kt` - Color definitions
- `ui/theme/Typography.kt` - Typography system
- `ui/theme/Dimens.kt` - Dimension constants
- `ui/theme/Theme.kt` - Material3 theme configuration

### Testing Files
- `ui/components/TestUtils.kt` - QA testing utilities
- Test tags: `movie_card`, `button_retry`, `movie_list_id`

## FAQ

**Q: How to add new server-driven component?**
A: Extend `ConfigurableText` pattern, add fallback to Material3 defaults.

**Q: How to test UI components?**
A: Use `TestUtils` modifiers and test tags from `TestUtils.kt`.

**Q: How to handle different screen sizes?**
A: Use `AdaptiveLayout` component with foldable device detection.

**Q: How to implement dynamic theming?**
A: Use `uiConfig` from backend response, fallback to Material3 when null.
