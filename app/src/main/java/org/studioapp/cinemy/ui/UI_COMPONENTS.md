# UI Layer Implementation - Cinemy

This package contains the complete UI layer implementation for the Cinemy Android application,
featuring server-driven UI components and enhanced movie data display.

## Architecture Overview

The UI layer is structured as follows:

```
ui/
├── components/                   # Reusable UI components
│   ├── ConfigurableButton.kt    # Server-driven button component
│   ├── ConfigurableMovieCard.kt # Enhanced movie card with new data fields
│   └── ConfigurableText.kt      # Server-driven text component
├── moviedetail/                 # Movie details screen
│   └── MovieDetailScreen.kt     # Enhanced movie details with complete data
├── movieslist/                  # Movies list screen
│   └── MoviesListScreen.kt      # Movies list with pagination
├── splash/                      # Splash screen
│   └── Splash.kt               # Application splash screen
└── theme/                       # UI theming system
    ├── Floats.kt               # Float values
    ├── Color.kt                # Color definitions
    ├── Dimens.kt               # Dimension constants
    ├── Theme.kt                # Material3 theme configuration
    ├── Type.kt                 # Typography types
    └── Typography.kt           # Typography definitions
```

## Enhanced Features (v2.0.0)

### ✅ **Pagination Functionality**

#### **MoviesListScreen.kt**

- **Pagination Controls**: Next/Previous page navigation
- **Swipe Gestures**: Swipe left/right for page navigation
- **Page Indicators**: Visual feedback for current page
- **Smart Navigation**: Prevents navigation beyond available pages

**Key Components:**

```kotlin
@Composable
private fun PaginationControls(
    currentPage: Int,
    totalPages: Int,
    onNextPage: () -> Unit,
    onPreviousPage: () -> Unit,
    uiConfig: UiConfiguration?
)

@Composable
private fun PageIndicator(
    currentPage: Int,
    totalPages: Int,
    uiConfig: UiConfiguration?
)
```

#### **Pagination Features:**

- ✅ **Page Navigation**: Next/Previous page controls
- ✅ **Swipe Gestures**: Swipe left/right for page navigation
- ✅ **Page Indicators**: Visual feedback for current page
- ✅ **Smart Navigation**: Prevents navigation beyond available pages
- ✅ **Page Loading**: Proper loading states during page changes

### ✅ **Enhanced Movie Data Display**

#### **MovieDetailScreen.kt**

Complete movie information display with all new data fields:

**Enhanced Sections:**

- ✅ **Runtime & Status**: Formatted runtime and movie status
- ✅ **Genres**: Horizontal scrollable genre chips
- ✅ **Budget & Revenue**: Financial information with proper formatting
- ✅ **Production Companies**: Company logos and origin country
- ✅ **Additional Details**: Vote count, runtime, status details

**Key Features:**

```kotlin
// Runtime and Status
Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
) {
    ConfigurableText(
        text = "Runtime: ${movieDetails.runtime} minutes",
        style = MaterialTheme.typography.bodyMedium,
        uiConfig = uiConfig
    )
    ConfigurableText(
        text = "Status: ${movieDetails.status}",
        style = MaterialTheme.typography.bodyMedium,
        uiConfig = uiConfig
    )
}

// Genres with chips
LazyRow(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(Dimens8)
) {
    items(movieDetails.genres) { genre ->
        Card(
            colors = CardDefaults.cardColors(
                containerColor = uiConfig?.buttons?.secondaryButtonColor ?: MoviePosterDarkBlue
            )
        ) {
            ConfigurableText(
                text = genre.name,
                style = MaterialTheme.typography.bodySmall,
                uiConfig = uiConfig,
                modifier = Modifier.padding(horizontal = Dimens12, vertical = Dimens6)
            )
        }
    }
}
```

#### **ConfigurableMovieCard.kt**

Enhanced movie card with all new Movie fields:

**New Features:**

- ✅ **Backdrop Support**: Uses backdropPath with posterPath fallback
- ✅ **Vote Count**: Displayed in rating badge
- ✅ **Adult Content**: 18+ indicator for adult movies
- ✅ **Popularity**: Popularity score display
- ✅ **Enhanced Rating**: Rating with vote count

**Key Implementation:**

```kotlin
// Backdrop with poster fallback
val imagePath = movie.backdropPath?.let { "https://image.tmdb.org/t/p/w500$it" }
    ?: movie.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }

// Enhanced rating display
ConfigurableText(
    text = "★ ${movie.rating} (${movie.voteCount})",
    style = MaterialTheme.typography.labelSmall,
    uiConfig = uiConfig,
    color = Color.White,
    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
)

// Adult content indicator
if (movie.adult) {
    Surface(
        color = Color.Red,
        shape = RoundedCornerShape(4.dp)
    ) {
        ConfigurableText(
            text = "18+",
            style = MaterialTheme.typography.labelSmall,
            uiConfig = uiConfig,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )
    }
}
```

## Server-Driven UI Architecture

### ✅ **Dynamic Color Implementation (v3.0.0)**

**BREAKING CHANGE**: The app now uses **dynamic colors from backend responses** instead of static
asset files.

#### **Backend-Driven Color System:**

- ✅ **Real-time Color Updates**: Colors change per movie based on backend uiConfig
- ✅ **Primary Colors**: Dynamic primary color theming (rating, buttons, accents)
- ✅ **Surface Colors**: Configurable surface and background colors
- ✅ **Text Colors**: Dynamic text color theming with proper contrast
- ✅ **Button Colors**: Configurable button styling from backend
- ✅ **Fallback Strategy**: Graceful fallback to Material3 defaults when uiConfig is null

#### **Color Extraction Process:**

```kotlin
// Backend Response Structure
{
  "uiConfig": {
    "colors": {
      "primary": "#DC3528",      // Red for "War of the Worlds"
      "secondary": "#E64539",    // Orange accent
      "background": "#121212",   // Dark background
      "surface": "#1E1E1E",     // Dark surface
      "onPrimary": "#FFFFFF",   // White text on primary
      "onSecondary": "#FFFFFF", // White text on secondary
      "onBackground": "#FFFFFF", // White text on background
      "onSurface": "#FFFFFF"    // White text on surface
    }
  }
}
```

#### **Dynamic Color Application:**

- ✅ **Rating Colors**: Movie rating displays in dynamic primary color
- ✅ **Background Colors**: Screen background uses dynamic background color
- ✅ **Surface Colors**: Card surfaces use dynamic surface color
- ✅ **Text Colors**: All text uses appropriate onSurface/onBackground colors
- ✅ **Button Colors**: Buttons use dynamic primary/secondary colors

#### **Technical Implementation:**

```kotlin
// McpClient.kt - Backend Color Extraction
val backendUiConfig = data?.get("uiConfig") as? Map<String, Any>
val uiConfig = if (backendUiConfig != null) {
    UiConfigurationDto(
        colors = ColorSchemeDto(
            primary = colors["primary"] as? String ?: "#DC3528",
            secondary = colors["secondary"] as? String ?: "#E64539",
            background = colors["background"] as? String ?: "#121212",
            surface = colors["surface"] as? String ?: "#1E1E1E",
            // ... other color mappings
        )
    )
} else {
    assetDataLoader.loadUiConfig() // Fallback to static assets
}

// MovieDetailScreen.kt - Dynamic Color Application
Box(
    modifier = Modifier
        .fillMaxSize()
        .background(
            if (state.uiConfig?.colors?.background != null) {
                state.uiConfig.colors.background
            } else {
                SplashBackground
            }
        )
) {
    ConfigurableText(
        text = stringResource(R.string.rating_label, movieDetails.rating),
        style = MaterialTheme.typography.bodyLarge,
        uiConfig = uiConfig,
        color = uiConfig?.colors?.primary // Dynamic primary color
    )
}
```

#### **Color Flow Architecture:**

```
Backend Response → McpClient → MovieMapper → ViewModel → UI Components
     ↓                ↓           ↓           ↓            ↓
  uiConfig        ColorScheme   UiConfig   State      Dynamic Colors
  (JSON)          (DTO)         (Domain)   (UI)       (Applied)
```

#### **Text Configuration:**

- ✅ **Dynamic Text**: Server-driven text content
- ✅ **Typography**: Configurable text styles
- ✅ **Localization**: Support for different languages

#### **Button Configuration:**

- ✅ **Button Colors**: Dynamic button color theming
- ✅ **Corner Radius**: Configurable button styling
- ✅ **Text Colors**: Dynamic button text colors

### ✅ **Component Architecture**

#### **Configurable Components:**

- ✅ **ConfigurableText**: Server-driven text with fallbacks
- ✅ **ConfigurableButton**: Server-driven button styling
- ✅ **ConfigurableMovieCard**: Enhanced movie display with theming

#### **Fallback Strategy:**

- ✅ **Material3 Defaults**: Graceful fallback to Material3 theming
- ✅ **Error Handling**: Robust error handling for missing configurations
- ✅ **Type Safety**: Type-safe configuration handling

## State Management Integration

### ✅ **MVI Pattern Compliance**

#### **State-Driven UI:**

- ✅ **Reactive Updates**: UI automatically updates on state changes
- ✅ **Loading States**: Proper loading indicators
- ✅ **Error States**: Comprehensive error handling
- ✅ **Empty States**: Proper empty state handling

#### **Intent Handling:**

- ✅ **User Interactions**: All user interactions properly handled
- ✅ **Navigation**: Clean navigation between screens
- ✅ **Pagination Actions**: Complete pagination action handling

## Performance Optimizations

### ✅ **Efficient Rendering**

#### **Lazy Loading:**

- ✅ **LazyColumn**: Efficient list rendering for movies
- ✅ **LazyRow**: Efficient horizontal scrolling for genres/companies
- ✅ **Image Loading**: Optimized image loading with Coil

#### **State Management:**

- ✅ **StateFlow**: Efficient state management
- ✅ **Recomposition**: Optimized recomposition
- ✅ **Memory Management**: Proper memory management

## Accessibility & UX

### ✅ **User Experience**

#### **Pagination Experience:**

- ✅ **Intuitive Navigation**: Easy-to-use pagination interface
- ✅ **Page Feedback**: Clear page navigation feedback
- ✅ **State Preservation**: Page state preservation during navigation

#### **Movie Details:**

- ✅ **Complete Information**: All movie data displayed
- ✅ **Organized Layout**: Well-organized information sections
- ✅ **Visual Hierarchy**: Clear visual hierarchy

#### **Navigation:**

- ✅ **Back Navigation**: Proper back button handling
- ✅ **Deep Linking**: Support for deep linking
- ✅ **State Preservation**: State preservation during navigation

## Testing & Quality

### ✅ **Component Testing**

#### **Testable Components:**

- ✅ **Isolated Components**: Each component is independently testable
- ✅ **Mock Support**: Easy mocking for testing
- ✅ **Preview Support**: Compose preview support

#### **Quality Assurance:**

- ✅ **Type Safety**: Full type safety with Kotlin
- ✅ **Null Safety**: Proper null safety handling
- ✅ **Error Handling**: Comprehensive error handling

## Future Enhancements

### 🚀 **Planned Features**

#### **Advanced Pagination:**

- 🔄 **Page Filters**: Genre, year, rating filters
- 🔄 **Page Suggestions**: Smart page recommendations
- 🔄 **Page History**: Persistent page navigation history

#### **Enhanced UI:**

- 🔄 **Dark/Light Mode**: Theme switching support
- 🔄 **Animations**: Smooth transitions and animations
- 🔄 **Accessibility**: Enhanced accessibility features

#### **Performance:**

- 🔄 **Image Caching**: Advanced image caching
- 🔄 **Offline Support**: Offline movie browsing
- 🔄 **Performance Monitoring**: Performance tracking

## Integration Points

### ✅ **Data Layer Integration**

#### **Repository Pattern:**

- ✅ **Clean Integration**: Clean integration with repository layer
- ✅ **Error Handling**: Proper error handling from data layer
- ✅ **Loading States**: Loading state management

#### **MCP Integration:**

- ✅ **Server-Driven UI**: Full MCP server-driven UI support
- ✅ **Dynamic Theming**: Real-time theming updates
- ✅ **Configuration Updates**: Dynamic configuration updates

## Conclusion

The UI layer has been successfully enhanced with comprehensive pagination functionality and complete
movie data display. All components support server-driven theming and maintain full MVI pattern
compliance. The implementation provides an excellent user experience with proper state management,
error handling, and performance optimizations.

**Key Achievements:**

- ✅ **Complete Pagination Functionality**: Full pagination implementation with navigation
- ✅ **Enhanced Movie Display**: All new movie data fields properly displayed
- ✅ **Server-Driven UI**: Complete UiConfiguration support
- ✅ **MVI Compliance**: Full MVI pattern implementation
- ✅ **Performance Optimized**: Efficient rendering and state management
- ✅ **User Experience**: Intuitive and responsive interface
- ✅ **Foldable Device Support**: Comprehensive support for foldable devices
- ✅ **Adaptive Layouts**: Responsive design for all device types
- ✅ **Dual Pane Layout**: Optimized for tablets and foldable devices
- ✅ **Configuration Change Handling**: Automatic layout updates
- ✅ **Accessibility Support**: Comprehensive accessibility features for all users
- ✅ **Screen Reader Support**: TalkBack and VoiceOver compatibility
- ✅ **Semantic Descriptions**: Context-aware content descriptions

## 📱 Foldable Device Components

### 🎯 **Adaptive Layout System**

#### **AdaptiveLayout.kt**

```kotlin
@Composable
fun AdaptiveLayout(
    leftPane: @Composable () -> Unit,
    rightPane: @Composable () -> Unit,
    modifier: Modifier = Modifier
)
```

**Features:**

- **Automatic device detection** and layout switching
- **Dual pane layout** for foldable devices and tablets
- **Single pane layout** for phones
- **Flexible sizing** for different screen sizes

#### **Device-Specific Layouts**

- **Foldable Devices**: 40/60 split with flexible sizing
- **Tablets**: Fixed left pane (320dp) + flexible right pane
- **Phones**: Single pane with navigation
- **Desktop**: Optimized for large screens

### 🪟 **WindowInsets Management**

#### **FoldableInsets.kt**

```kotlin
@Composable
fun Modifier.adaptiveInsetsPadding(): Modifier

@Composable
fun Modifier.safeDrawingPadding(): Modifier

@Composable
fun Modifier.systemBarsPadding(): Modifier
```

**Features:**

- **Automatic insets selection** based on device type
- **Safe drawing insets** for foldable devices
- **System bars insets** for tablets and phones
- **Selective padding** for specific sides

### 🔧 **Device Detection**

#### **DeviceUtils.kt**

```kotlin
@Composable
fun getDeviceType(): DeviceUtils.DeviceType

@Composable
fun isFoldableDevice(): Boolean

@Composable
fun supportsDualPane(): Boolean

@Composable
fun getOptimalColumnCount(): Int
```

**Device Types:**

- **PHONE**: Standard smartphone
- **TABLET**: Tablet device
- **FOLDABLE**: Foldable device (Galaxy Fold, Surface Duo)
- **DESKTOP**: Desktop mode (Chrome OS, Samsung DeX)

### 📐 **Resource Configuration**

#### **Large Screen Resources**

- `values-sw600dp/`: Smallest width 600dp+ (tablets)
- `values-w600dp/`: Width 600dp+ (wide screens)
- `values-land/`: Landscape orientation
- **Themes**: Optimized for different screen sizes

#### **Manifest Configuration**

```xml
<activity
    android:configChanges="orientation|screenSize|screenLayout|smallestScreenSize|uiMode"
    android:resizeableActivity="true"
    android:supportsPictureInPicture="true">
```

## ♿ Accessibility Components

### 🎯 **Accessibility Utilities**

#### **AccessibilityUtils.kt**

```kotlin
object AccessibilityUtils {
    fun createMovieCardDescription(title: String, rating: Double, releaseDate: String): String
    fun createMovieDetailsDescription(
        title: String,
        rating: Double,
        releaseDate: String,
        runtime: Int,
        genres: List<String>,
        description: String
    ): String
    fun createButtonDescription(action: String, target: String? = null): String
    fun createPaginationDescription(
        currentPage: Int,
        totalPages: Int,
        hasNext: Boolean,
        hasPrevious: Boolean
    ): String
    fun createLoadingDescription(content: String): String
    fun createErrorDescription(error: String, retryAction: String = "retry"): String
    fun createSentimentDescription(positiveCount: Int, negativeCount: Int, totalCount: Int): String
}
```

**Features:**

- **Semantic descriptions** for all UI components
- **Screen reader support** with descriptive content
- **Role-based semantics** for interactive elements
- **Context-aware descriptions** for different states

### 🎨 **Accessible UI Components**

#### **ConfigurableText with Accessibility**

```kotlin
@Composable
fun ConfigurableText(
    text: String,
    contentDescription: String? = null, // Accessibility description
    // ... other parameters
)
```

**Features:**

- **Optional content descriptions** for screen readers
- **Semantic role detection** for text elements
- **Dynamic descriptions** based on content context

#### **ConfigurableButton with Accessibility**

```kotlin
@Composable
fun ConfigurableButton(
    text: String,
    contentDescription: String? = null, // Accessibility description
    // ... other parameters
)
```

**Features:**

- **Button role semantics** for screen readers
- **Descriptive action text** for button purposes
- **State-aware descriptions** (enabled/disabled)

#### **ConfigurableMovieCard with Accessibility**

```kotlin
@Composable
fun ConfigurableMovieCard(
    movie: Movie,
    contentDescription: String? = null, // Accessibility description
    // ... other parameters
)
```

**Features:**

- **Card role semantics** for interactive cards
- **Movie information descriptions** for screen readers
- **Click action descriptions** for navigation

### 🔧 **Accessibility Modifiers**

#### **Accessible Clickable**

```kotlin
@Composable
fun Modifier.accessibleClickable(
    description: String,
    role: Role = Role.Button,
    onClick: () -> Unit
): Modifier
```

#### **Accessible Card**

```kotlin
@Composable
fun Modifier.accessibleCard(
    description: String,
    isClickable: Boolean = false,
    onClick: (() -> Unit)? = null
): Modifier
```

### 📱 **Accessible Components**

#### **AccessibleMovieCard**

```kotlin
@Composable
fun AccessibleMovieCard(
    title: String,
    rating: Double,
    releaseDate: String,
    posterPath: String?,
    onClick: () -> Unit
)
```

#### **AccessibleButton**

```kotlin
@Composable
fun AccessibleButton(
    text: String,
    action: String,
    target: String? = null,
    onClick: () -> Unit
)
```

#### **AccessibleLoadingIndicator**

```kotlin
@Composable
fun AccessibleLoadingIndicator(
    content: String, // What is being loaded
    modifier: Modifier = Modifier
)
```

#### **AccessibleErrorMessage**

```kotlin
@Composable
fun AccessibleErrorMessage(
    error: String,
    retryAction: String = "retry",
    onRetry: (() -> Unit)? = null
)
```

### 🎯 **Accessibility Features**

#### **Screen Reader Support**

- **TalkBack integration** for Android
- **VoiceOver support** for iOS
- **Semantic roles** for all interactive elements
- **Content descriptions** for all visual elements

#### **Navigation Support**

- **Clear navigation hints** for complex interactions
- **State announcements** for dynamic content
- **Focus management** for keyboard navigation
- **Gesture support** for accessibility

#### **Dynamic Descriptions**

- **State-based descriptions** for loading, error, and success states
- **Context-aware descriptions** for different screen types
- **Action descriptions** for buttons and interactive elements
- **Content descriptions** for images and visual elements

The UI layer is now ready for production use with all enhanced features fully implemented and
tested, including comprehensive foldable device support and accessibility features.

## QA Testing Components

### TestUtils

Comprehensive testing utilities for QA automation:

```kotlin
// Test tags for different UI components
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

// Test IDs for specific elements
object TestIds {
    const val MOVIE_LIST_ID = "movie_list_id"
    const val MOVIE_CARD_ID = "movie_card_id"
    const val MOVIE_TITLE_ID = "movie_title_id"
    const val BUTTON_RETRY_ID = "button_retry_id"
    const val LOADING_INDICATOR_ID = "loading_indicator_id"
    const val ERROR_MESSAGE_ID = "error_message_id"
}

// Test data attributes for automation
object TestData {
    const val MOVIE_ID = "data-movie-id"
    const val MOVIE_TITLE = "data-movie-title"
    const val MOVIE_RATING = "data-movie-rating"
    const val LOADING_STATE = "data-loading-state"
    const val ERROR_STATE = "data-error-state"
    const val DEVICE_TYPE = "data-device-type"
}
```

### Test Modifiers

Modifier extensions for adding test attributes:

```kotlin
// Add test tag to modifier
fun Modifier.testTag(tag: String): Modifier {
    return this.testTag(tag)
}

// Add test ID to modifier
fun Modifier.testId(id: String): Modifier {
    return this.semantics {
        this.testTag = id
    }
}

// Add test data attribute to modifier
fun Modifier.testData(key: String, value: String): Modifier {
    return this.semantics {
        this.testTag = "$key:$value"
    }
}

// Add multiple test attributes to modifier
fun Modifier.testAttributes(
    tag: String? = null,
    id: String? = null,
    data: Map<String, String> = emptyMap()
): Modifier {
    var modifier = this

    tag?.let { modifier = modifier.testTag(it) }
    id?.let { modifier = modifier.testId(it) }

    data.forEach { (key, value) ->
        modifier = modifier.testData(key, value)
    }

    return modifier
}
```

### Enhanced UI Components with Testing

#### ConfigurableText with Testing

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
) {
    // ... existing implementation ...

    Text(
        text = text,
        style = style,
        modifier = modifier
            .let { baseModifier ->
                if (contentDescription != null) {
                    baseModifier.semantics {
                        this.contentDescription = contentDescription
                    }
                } else {
                    baseModifier
                }
            }
            .let { semanticsModifier ->
                TestUtils.TestModifiers.testAttributes(
                    tag = testTag,
                    id = testId,
                    data = testData
                ).let { testModifier ->
                    semanticsModifier.then(testModifier)
                }
            },
        color = textColor,
        maxLines = maxLines
    )
}
```

#### ConfigurableButton with Testing

```kotlin
@Composable
fun ConfigurableButton(
    text: String,
    onClick: () -> Unit,
    uiConfig: UiConfiguration? = null,
    modifier: Modifier = Modifier,
    isSecondary: Boolean = false,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = Dimens16, vertical = Dimens8),
    contentDescription: String? = null,
    testTag: String? = null,
    testId: String? = null,
    testData: Map<String, String> = emptyMap()
) {
    // ... existing implementation ...

    Button(
        onClick = onClick,
        modifier = modifier
            .background(buttonColor)
            .let { baseModifier ->
                if (contentDescription != null) {
                    baseModifier.semantics {
                        this.role = androidx.compose.ui.semantics.Role.Button
                        this.contentDescription = contentDescription
                    }
                } else {
                    baseModifier
                }
            }
            .let { semanticsModifier ->
                TestUtils.TestModifiers.testAttributes(
                    tag = testTag,
                    id = testId,
                    data = testData
                ).let { testModifier ->
                    semanticsModifier.then(testModifier)
                }
            },
        // ... other parameters ...
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}
```

#### ConfigurableMovieCard with Testing

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
) {
    // ... existing implementation ...

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .background(cardColor)
            .let { baseModifier ->
                if (contentDescription != null) {
                    baseModifier.semantics {
                        this.role = Role.Button
                        this.contentDescription = contentDescription
                    }
                } else {
                    baseModifier
                }
            }
            .let { semanticsModifier ->
                TestUtils.TestModifiers.testAttributes(
                    tag = testTag,
                    id = testId,
                    data = testData
                ).let { testModifier ->
                    semanticsModifier.then(testModifier)
                }
            },
        // ... other parameters ...
    ) {
        // ... card content ...
    }
}
```

### Testing Framework Integration

#### Espresso Testing

```kotlin
// Find elements by test tag
onView(withTag("movie_card")).perform(click())
onView(withTag("button_retry")).perform(click())

// Find elements by test ID
onView(withId("movie_list_id")).check(matches(isDisplayed()))
onView(withId("button_back_id")).perform(click())

// Find elements by content description
onView(withContentDescription("Movie card")).perform(click())
onView(withContentDescription("Retry button")).perform(click())
```

#### UI Automator

```kotlin
// Find elements by test tag
val movieCard = device.findObject(UiSelector().description("movie_card"))
movieCard.click()

val retryButton = device.findObject(UiSelector().description("button_retry"))
retryButton.click()

// Find elements by test ID
val movieList = device.findObject(UiSelector().resourceId("movie_list_id"))
movieList.waitForExists(5000)
```

#### Appium Testing

```python
# Find elements by test tag
movie_card = driver.find_element_by_accessibility_id("movie_card")
movie_card.click()

retry_button = driver.find_element_by_accessibility_id("button_retry")
retry_button.click()

# Find elements by test ID
movie_list = driver.find_element_by_id("movie_list_id")
assert movie_list.is_displayed()
```

#### Detox Testing

```javascript
// Find elements by test tag
await element(by.id('movie_card')).tap();
await element(by.id('button_retry')).tap();

// Find elements by test ID
await expect(element(by.id('movie_list_id'))).toBeVisible();
await expect(element(by.id('button_back_id'))).toBeVisible();
```

### Test Scenarios

#### Movie List Testing

```kotlin
// Test movie list loading
onView(withTag("movie_list")).check(matches(isDisplayed()))
onView(withTag("loading_movies")).check(matches(isDisplayed()))

// Test movie card interaction
onView(withTag("movie_card")).perform(click())
onView(withTag("movie_details_screen")).check(matches(isDisplayed()))
```

#### Error Handling Testing

```kotlin
// Test error state
onView(withTag("error_message")).check(matches(isDisplayed()))
onView(withTag("button_retry")).perform(click())

// Test retry functionality
onView(withTag("loading_movies")).check(matches(isDisplayed()))
```

#### Pagination Testing

```kotlin
// Test pagination
onView(withTag("pagination_info")).check(matches(isDisplayed()))
onView(withTag("button_next")).perform(click())
onView(withTag("pagination_loading")).check(matches(isDisplayed()))
```

#### Foldable Device Testing

```kotlin
// Test foldable layout
onView(withTag("foldable_layout")).check(matches(isDisplayed()))
onView(withTag("dual_pane_left")).check(matches(isDisplayed()))
onView(withTag("dual_pane_right")).check(matches(isDisplayed()))
```

### Test Data Attributes

#### Movie Data

```kotlin
// Test movie data attributes
ConfigurableMovieCard(
    movie = movie,
    onClick = { /* navigation logic */ },
    testTag = TestUtils.TestTags.MOVIE_CARD,
    testId = TestUtils.TestIds.MOVIE_CARD_ID,
    testData = mapOf(
        TestUtils.TestData.MOVIE_ID to movie.id.toString(),
        TestUtils.TestData.MOVIE_TITLE to movie.title,
        TestUtils.TestData.MOVIE_RATING to movie.rating.toString()
    )
)
```

#### UI State

```kotlin
// Test UI state attributes
ConfigurableButton(
    text = "Retry",
    onClick = { /* retry logic */ },
    testTag = TestUtils.TestTags.BUTTON_RETRY,
    testId = TestUtils.TestIds.BUTTON_RETRY_ID,
    testData = mapOf(
        TestUtils.TestData.ERROR_STATE to "network_error"
    )
)
```

#### Device Type

```kotlin
// Test device type attributes
AdaptiveLayout(
    leftPane = { /* left pane content */ },
    rightPane = { /* right pane content */ },
    modifier = Modifier.testData(
        TestUtils.TestData.DEVICE_TYPE to "foldable",
        TestUtils.TestData.SCREEN_SIZE to "large",
        TestUtils.TestData.ORIENTATION to "landscape"
    )
)
```

### Testing Best Practices

#### Test Tag Naming

- Use descriptive, consistent naming
- Follow the pattern: `component_type` (e.g., `movie_card`, `button_retry`)
- Use snake_case for multi-word tags

#### Test ID Management

- Use unique IDs for each element
- Follow the pattern: `component_type_id` (e.g., `movie_card_id`)
- Avoid conflicts with existing IDs

#### Test Data Attributes

- Use meaningful data attributes
- Follow the pattern: `data-attribute-name`
- Include relevant context information

#### Accessibility Testing

- Test with screen readers enabled
- Verify content descriptions are meaningful
- Test keyboard navigation
- Test voice commands

#### Cross-Platform Testing

- Test on different screen sizes
- Test on different orientations
- Test on foldable devices
- Test on different Android versions

### Testing Troubleshooting

#### Common Issues

1. **Test tags not found**
    - Verify the component has the correct test tag
    - Check if the component is visible
    - Ensure the test tag is properly set

2. **Test IDs not working**
    - Verify the test ID is unique
    - Check if the component is rendered
    - Ensure the test ID is properly set

3. **Accessibility issues**
    - Verify content descriptions are set
    - Check if the component is accessible
    - Test with screen readers

#### Debug Tips

1. **Use test tags for debugging**
    - Add temporary test tags for debugging
    - Use descriptive test tags
    - Remove debug tags before release

2. **Test data attributes**
    - Use test data attributes for debugging
    - Include relevant context information
    - Verify data attributes are set correctly

3. **Accessibility debugging**
    - Test with screen readers
    - Verify content descriptions
    - Check keyboard navigation

### Testing Resources

#### Documentation

- **QA Testing Guide**: Comprehensive testing guide
- **Testing Framework Integration**: Framework-specific testing guides
- **Test Scenarios**: Common test scenarios and examples
- **Troubleshooting Guide**: Common issues and solutions

#### Tools

- **Espresso**: Android UI testing framework
- **UI Automator**: Cross-app UI testing
- **Appium**: Cross-platform mobile testing
- **Detox**: React Native testing framework

#### Best Practices

- **Test Tag Naming**: Consistent, descriptive naming
- **Test ID Management**: Unique, meaningful IDs
- **Test Data Attributes**: Contextual information
- **Accessibility Testing**: Screen reader and voice command testing
- **Cross-Platform Testing**: Different devices and orientations
