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

**BREAKING CHANGE**: The app now uses **dynamic colors from backend responses** instead of static asset files.

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

The UI layer is now ready for production use with all enhanced features fully implemented and
tested, including comprehensive foldable device support.
