# UI Components Layer Implementation

This package contains the complete UI components layer implementation for the Cinemy Android
application, featuring server-driven UI components, adaptive layouts, and comprehensive theming system.

## Architecture Overview

The UI components layer is structured as follows:

```
ui/
├── components/                   # Reusable UI components
│   ├── AdaptiveLayout.kt         # Adaptive layout for foldable devices
│   ├── ConfigurableMovieCard.kt  # Server-driven movie card component
│   ├── ConfigurableText.kt       # Server-driven text component
│   ├── PullToReloadIndicator.kt  # Pull-to-refresh indicator
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

## Key Components

### 1. Adaptive Layout System (`AdaptiveLayout.kt`)

#### AdaptiveLayout Component

- **Purpose**: Adaptive layout component that adjusts based on device type
- **Device Support**: Foldable devices, tablets, phones, desktop
- **Layout Modes**: Single pane, dual pane, foldable dual pane
- **Responsive Design**: Automatic layout switching based on screen size

#### Key Features

- **Device Detection**: Automatic device type detection
- **Layout Selection**: Intelligent layout selection based on device capabilities
- **Spacing Optimization**: Optimal spacing for different device types
- **Foldable Support**: Special handling for foldable devices

#### Layout Modes

```kotlin
@Composable
fun AdaptiveLayout(
    leftPane: @Composable () -> Unit,
    rightPane: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    showRightPane: Boolean = false
)
```

**Layout Types:**

- **Foldable Dual Pane**: 40/60 split with flexible sizing for unfolded foldable devices
- **Standard Dual Pane**: Fixed left pane (320dp) + flexible right pane for tablets
- **Single Pane**: Full-width layout for phones and folded foldable devices

### 2. Server-Driven Components

#### ConfigurableText Component

- **Purpose**: Server-driven text component with dynamic styling
- **Theming**: Dynamic color theming from backend configuration
- **Accessibility**: Screen reader support with content descriptions
- **Testing**: QA automation support with test tags and IDs

#### Key Features

```kotlin
@Composable
fun ConfigurableText(
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    uiConfig: UiConfiguration? = null,
    modifier: Modifier = Modifier,
    color: Color? = null,
    maxLines: Int = Int.MAX_VALUE,
    contentDescription: String? = null,
    testTag: String? = null,
    testId: String? = null,
    testData: Map<String, String> = emptyMap()
)
```

**Features:**

- **Dynamic Theming**: Server-driven color theming
- **Fallback Strategy**: Material3 defaults when no configuration provided
- **Accessibility Support**: Content descriptions for screen readers
- **QA Testing**: Test tags, IDs, and data attributes for automation

#### ConfigurableMovieCard Component

- **Purpose**: Enhanced movie card with server-driven styling
- **Data Display**: Complete movie information with all new fields
- **Image Support**: Backdrop with poster fallback
- **Rating Display**: Enhanced rating with vote count

#### Key Features

```kotlin
@Composable
fun ConfigurableMovieCard(
    movie: Movie,
    onClick: () -> Unit,
    uiConfig: UiConfiguration? = null,
    modifier: Modifier = Modifier,
    showRating: Boolean = true,
    showReleaseDate: Boolean = true,
    contentDescription: String? = null,
    testTag: String? = null,
    testId: String? = null,
    testData: Map<String, String> = emptyMap()
)
```

**Features:**

- **Backdrop Support**: Uses backdropPath with posterPath fallback
- **Vote Count**: Displayed in rating badge
- **Adult Content**: 18+ indicator for adult movies
- **Popularity**: Popularity score display
- **Enhanced Rating**: Rating with vote count
- **Server-Driven Theming**: Dynamic colors from backend

### 3. Sentiment Analysis Components

#### SentimentAnalysisCard Component

- **Purpose**: Card component for displaying sentiment analysis results
- **Data Display**: Sentiment reviews with positive/negative counts
- **Visual Indicators**: Color-coded sentiment indicators
- **Error Handling**: Error message display for failed analysis

#### Key Features

```kotlin
@Composable
fun SentimentAnalysisCard(
    sentimentReviews: SentimentReviews? = null,
    error: String? = null,
    modifier: Modifier = Modifier
)
```

**Features:**

- **Sentiment Display**: Positive and negative review counts
- **Visual Indicators**: Color-coded sentiment bars
- **Error Handling**: Error message display
- **Responsive Design**: Adaptive layout for different screen sizes

### 4. Pull-to-Refresh Components

#### PullToReloadIndicator Component

- **Purpose**: Pull-to-refresh indicator for content reloading
- **Visual Feedback**: Loading indicator during refresh
- **State Management**: Loading state handling
- **User Experience**: Smooth pull-to-refresh interaction

#### Key Features

```kotlin
@Composable
fun PullToReloadIndicator(
    isRefreshing: Boolean,
    modifier: Modifier = Modifier
)
```

**Features:**

- **Visual Feedback**: Loading indicator during refresh
- **State Management**: Proper loading state handling
- **Smooth Interaction**: Smooth pull-to-refresh experience
- **Accessibility**: Screen reader support

### 5. Screen Components

#### MoviesListScreen Component

- **Purpose**: Movies list screen with pagination
- **Pagination**: Next/Previous page navigation
- **Swipe Gestures**: Swipe left/right for page navigation
- **Page Indicators**: Visual feedback for current page
- **Smart Navigation**: Prevents navigation beyond available pages

#### Key Features

```kotlin
@Composable
fun MoviesListScreen(
    state: MoviesListState,
    onIntent: (MoviesListIntent) -> Unit,
    modifier: Modifier = Modifier
)
```

**Features:**

- **Pagination Controls**: Next/Previous page navigation
- **Swipe Gestures**: Swipe left/right for page navigation
- **Page Indicators**: Visual feedback for current page
- **Smart Navigation**: Prevents navigation beyond available pages
- **Loading States**: Proper loading state handling
- **Error Handling**: Comprehensive error handling

#### MovieDetailScreen Component

- **Purpose**: Enhanced movie details with complete data
- **Complete Information**: All movie data fields displayed
- **Organized Layout**: Well-organized information sections
- **Visual Hierarchy**: Clear visual hierarchy

#### Key Features

```kotlin
@Composable
fun MovieDetailScreen(
    state: MovieDetailState,
    onIntent: (MovieDetailIntent) -> Unit,
    modifier: Modifier = Modifier
)
```

**Features:**

- **Runtime & Status**: Formatted runtime and movie status
- **Genres**: Horizontal scrollable genre chips
- **Budget & Revenue**: Financial information with proper formatting
- **Production Companies**: Company logos and origin country
- **Additional Details**: Vote count, runtime, status details
- **Sentiment Analysis**: ML-powered sentiment analysis display

#### DualPaneScreen Component

- **Purpose**: Dual pane screen for tablets and foldable devices
- **Adaptive Layout**: Automatic layout switching
- **Content Management**: Left and right pane content
- **Navigation**: Seamless navigation between panes

#### Key Features

```kotlin
@Composable
fun DualPaneScreen(
    leftPane: @Composable () -> Unit,
    rightPane: @Composable () -> Unit,
    modifier: Modifier = Modifier
)
```

**Features:**

- **Adaptive Layout**: Automatic layout switching based on device
- **Content Management**: Left and right pane content
- **Navigation**: Seamless navigation between panes
- **Responsive Design**: Optimized for different screen sizes

### 6. Theming System

#### Color System

- **Dynamic Colors**: Server-driven color theming
- **Material3 Integration**: Material3 color scheme integration
- **Fallback Strategy**: Graceful fallback to Material3 defaults
- **Accessibility**: Proper contrast ratios for accessibility

#### Typography System

- **Material3 Typography**: Material3 typography integration
- **Custom Styles**: Custom text styles for specific use cases
- **Responsive Typography**: Typography that adapts to screen size
- **Accessibility**: Readable typography for all users

#### Dimension System

- **Consistent Spacing**: Consistent spacing throughout the app
- **Responsive Dimensions**: Dimensions that adapt to screen size
- **Accessibility**: Proper spacing for touch targets
- **Performance**: Optimized dimensions for performance

### 7. QA Testing System

#### TestUtils Component

- **Purpose**: Comprehensive testing utilities for QA automation
- **Test Tags**: Consistent test tag naming
- **Test IDs**: Unique test IDs for elements
- **Test Data**: Test data attributes for automation

#### Key Features

```kotlin
object TestTags {
    const val NAV_MOVIES = "nav_movies"
    const val NAV_DETAILS = "nav_details"
    const val MOVIE_LIST = "movie_list"
    const val MOVIE_CARD = "movie_card"
    const val MOVIE_TITLE = "movie_title"
    const val MOVIE_RATING = "movie_rating"
    const val BUTTON_RETRY = "button_retry"
    const val BUTTON_BACK = "button_back"
    const val LOADING_INDICATOR = "loading_indicator"
    const val ERROR_MESSAGE = "error_message"
    const val PAGINATION_INFO = "pagination_info"
    const val SENTIMENT_ANALYSIS = "sentiment_analysis"
    const val FOLDABLE_LAYOUT = "foldable_layout"
    const val ACCESSIBLE_TEXT = "accessible_text"
}
```

**Features:**

- **Test Tag Management**: Consistent test tag naming
- **Test ID Management**: Unique test IDs for elements
- **Test Data Attributes**: Test data attributes for automation
- **Accessibility Testing**: Screen reader and voice command testing
- **Cross-Platform Testing**: Different devices and orientations

## Server-Driven UI Architecture

### ✅ **Dynamic Color Implementation**

**Backend-Driven Color System:**

- **Real-time Color Updates**: Colors change per movie based on backend uiConfig
- **Primary Colors**: Dynamic primary color theming (rating, buttons, accents)
- **Surface Colors**: Configurable surface and background colors
- **Text Colors**: Dynamic text color theming with proper contrast
- **Button Colors**: Configurable button styling from backend
- **Fallback Strategy**: Graceful fallback to Material3 defaults when uiConfig is null

#### Color Flow Architecture

```
Backend Response → McpClient → MovieMapper → ViewModel → UI Components
     ↓                ↓           ↓           ↓            ↓
  uiConfig        ColorScheme   UiConfig   State      Dynamic Colors
  (JSON)          (DTO)         (Domain)   (UI)       (Applied)
```

#### Dynamic Color Application

- **Rating Colors**: Movie rating displays in dynamic primary color
- **Background Colors**: Screen background uses dynamic background color
- **Surface Colors**: Card surfaces use dynamic surface color
- **Text Colors**: All text uses appropriate onSurface/onBackground colors
- **Button Colors**: Buttons use dynamic primary/secondary colors

### ✅ **Component Architecture**

#### Configurable Components

- **ConfigurableText**: Server-driven text with fallbacks
- **ConfigurableButton**: Server-driven button styling
- **ConfigurableMovieCard**: Enhanced movie display with theming

#### Fallback Strategy

- **Material3 Defaults**: Graceful fallback to Material3 theming
- **Error Handling**: Robust error handling for missing configurations
- **Type Safety**: Type-safe configuration handling

## State Management Integration

### ✅ **MVI Pattern Compliance**

#### State-Driven UI

- **Reactive Updates**: UI automatically updates on state changes
- **Loading States**: Proper loading indicators
- **Error States**: Comprehensive error handling
- **Empty States**: Proper empty state handling

#### Intent Handling

- **User Interactions**: All user interactions properly handled
- **Navigation**: Clean navigation between screens
- **Pagination Actions**: Complete pagination action handling

## Performance Optimizations

### ✅ **Efficient Rendering**

#### Lazy Loading

- **LazyColumn**: Efficient list rendering for movies
- **LazyRow**: Efficient horizontal scrolling for genres/companies
- **Image Loading**: Optimized image loading with Coil

#### State Management

- **StateFlow**: Efficient state management
- **Recomposition**: Optimized recomposition
- **Memory Management**: Proper memory management

## Accessibility & UX

### ✅ **User Experience**

#### Pagination Experience

- **Intuitive Navigation**: Easy-to-use pagination interface
- **Page Feedback**: Clear page navigation feedback
- **State Preservation**: Page state preservation during navigation

#### Movie Details

- **Complete Information**: All movie data displayed
- **Organized Layout**: Well-organized information sections
- **Visual Hierarchy**: Clear visual hierarchy

#### Navigation

- **Back Navigation**: Proper back button handling
- **Deep Linking**: Support for deep linking
- **State Preservation**: State preservation during navigation

### ✅ **Accessibility Support**

#### Screen Reader Support

- **TalkBack Integration**: Android screen reader support
- **VoiceOver Support**: iOS screen reader support
- **Semantic Roles**: Semantic roles for all interactive elements
- **Content Descriptions**: Content descriptions for all visual elements

#### Navigation Support

- **Clear Navigation Hints**: Clear navigation hints for complex interactions
- **State Announcements**: State announcements for dynamic content
- **Focus Management**: Focus management for keyboard navigation
- **Gesture Support**: Gesture support for accessibility

#### Dynamic Descriptions

- **State-based Descriptions**: State-based descriptions for loading, error, and success states
- **Context-aware Descriptions**: Context-aware descriptions for different screen types
- **Action Descriptions**: Action descriptions for buttons and interactive elements
- **Content Descriptions**: Content descriptions for images and visual elements

## Testing & Quality

### ✅ **Component Testing**

#### Testable Components

- **Isolated Components**: Each component is independently testable
- **Mock Support**: Easy mocking for testing
- **Preview Support**: Compose preview support

#### Quality Assurance

- **Type Safety**: Full type safety with Kotlin
- **Null Safety**: Proper null safety handling
- **Error Handling**: Comprehensive error handling

### ✅ **Testing Framework Integration**

#### Espresso Testing

```kotlin
// Find elements by test tag
onView(withTag("movie_card")).perform(click())
onView(withTag("button_retry")).perform(click())

// Find elements by test ID
onView(withId("movie_list_id")).check(matches(isDisplayed()))
onView(withId("button_back_id")).perform(click())
```

#### UI Automator

```kotlin
// Find elements by test tag
val movieCard = device.findObject(UiSelector().description("movie_card"))
movieCard.click()

val retryButton = device.findObject(UiSelector().description("button_retry"))
retryButton.click()
```

#### Appium Testing

```python
# Find elements by test tag
movie_card = driver.find_element_by_accessibility_id("movie_card")
movie_card.click()

retry_button = driver.find_element_by_accessibility_id("button_retry")
retry_button.click()
```

## Integration Points

### ✅ **Data Layer Integration**

#### Repository Pattern

- **Clean Integration**: Clean integration with repository layer
- **Error Handling**: Proper error handling from data layer
- **Loading States**: Loading state management

#### MCP Integration

- **Server-Driven UI**: Full MCP server-driven UI support
- **Dynamic Theming**: Real-time theming updates
- **Configuration Updates**: Dynamic configuration updates

## Future Enhancements

### 🚀 **Planned Features**

#### Advanced Pagination

- **Page Filters**: Genre, year, rating filters
- **Page Suggestions**: Smart page recommendations
- **Page History**: Persistent page navigation history

#### Enhanced UI

- **Dark/Light Mode**: Theme switching support
- **Animations**: Smooth transitions and animations
- **Accessibility**: Enhanced accessibility features

#### Performance

- **Image Caching**: Advanced image caching
- **Offline Support**: Offline movie browsing
- **Performance Monitoring**: Performance tracking

## Dependencies

- **Jetpack Compose**: Modern UI toolkit
- **Material3**: Material Design 3 components
- **Coil**: Image loading library
- **Koin**: Dependency injection
- **Coroutines**: Asynchronous programming
- **Android Context**: Asset loading and resource access

## Build Status

✅ **BUILD SUCCESSFUL** - All UI components compile correctly and are fully integrated with the presentation layer.

✅ **SERVER-DRIVEN UI** - Complete server-driven UI with dynamic theming.

✅ **ADAPTIVE LAYOUTS** - Comprehensive support for foldable devices and tablets.

✅ **ACCESSIBILITY** - Full accessibility support with screen reader compatibility.

✅ **QA TESTING** - Comprehensive testing utilities for automation.

✅ **PERFORMANCE** - Optimized rendering and state management.

✅ **THEMING** - Complete theming system with Material3 integration.

The UI components layer is now ready for production use with all enhanced features fully implemented and tested, including comprehensive foldable device support, accessibility features, and server-driven theming.
