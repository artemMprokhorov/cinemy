# ♿ Accessibility Guide

## 🎯 Overview

This document describes the accessibility features implemented in the Cinemy project to ensure the app is usable by all users, including those with disabilities.

## ✅ Implemented Accessibility Features

### 🔧 Accessibility Utilities

#### **AccessibilityUtils.kt**
- **Semantic descriptions** for all UI components
- **Screen reader support** with descriptive content
- **Role-based semantics** for interactive elements
- **Context-aware descriptions** for different states

### 📱 UI Component Accessibility

#### **ConfigurableText.kt**
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

#### **ConfigurableButton.kt**
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

#### **ConfigurableMovieCard.kt**
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

### 🎨 Accessibility Components

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

**Features:**
- **Automatic description generation** from movie data
- **Rating and release date** in accessible format
- **Navigation instructions** for screen readers

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

**Features:**
- **Action-based descriptions** (e.g., "Retry loading movies")
- **Target context** for navigation buttons
- **Clear purpose indication** for screen readers

#### **AccessibleLoadingIndicator**
```kotlin
@Composable
fun AccessibleLoadingIndicator(
    content: String, // What is being loaded
    modifier: Modifier = Modifier
)
```

**Features:**
- **Loading state announcements** for screen readers
- **Progress indication** for long operations
- **Context-aware descriptions** for different loading states

#### **AccessibleErrorMessage**
```kotlin
@Composable
fun AccessibleErrorMessage(
    error: String,
    retryAction: String = "retry",
    onRetry: (() -> Unit)? = null
)
```

**Features:**
- **Error state announcements** for screen readers
- **Retry action descriptions** for error recovery
- **Clear error context** for user understanding

### 📊 Semantic Descriptions

#### **Movie Card Descriptions**
```kotlin
// Example: "Movie: The Dark Knight, Rating: 8.5 out of 10, Released: 2008-07-18, Double tap to view details"
fun createMovieCardDescription(
    title: String,
    rating: Double,
    releaseDate: String,
    isClickable: Boolean = true
): String
```

#### **Movie Details Descriptions**
```kotlin
// Example: "Movie Details: The Dark Knight, Rating: 8.5 out of 10, Released: 2008-07-18, Runtime: 152 minutes, Genres: Action, Crime, Drama, Description: When the menace known as the Joker..."
fun createMovieDetailsDescription(
    title: String,
    rating: Double,
    releaseDate: String,
    runtime: Int,
    genres: List<String>,
    description: String
): String
```

#### **Button Descriptions**
```kotlin
// Example: "Retry loading movies" or "Navigate to movie details"
fun createButtonDescription(
    action: String,
    target: String? = null
): String
```

#### **Pagination Descriptions**
```kotlin
// Example: "Page 2 of 10, Swipe left for next page"
fun createPaginationDescription(
    currentPage: Int,
    totalPages: Int,
    hasNext: Boolean,
    hasPrevious: Boolean
): String
```

#### **Loading State Descriptions**
```kotlin
// Example: "Loading movie details, please wait"
fun createLoadingDescription(content: String): String
```

#### **Error State Descriptions**
```kotlin
// Example: "Error: Network connection failed, retry to try again"
fun createErrorDescription(error: String, retryAction: String = "retry"): String
```

#### **Sentiment Analysis Descriptions**
```kotlin
// Example: "Sentiment Analysis: 5 positive reviews, 3 negative reviews, out of 8 total reviews"
fun createSentimentDescription(
    positiveCount: Int,
    negativeCount: Int,
    totalCount: Int
): String
```

### 🔄 Dynamic Accessibility

#### **State-Based Descriptions**
- **Loading states**: "Loading movies, please wait"
- **Error states**: "Error loading movies, retry to try again"
- **Empty states**: "No movies found, try refreshing"
- **Success states**: "Movies loaded successfully"

#### **Context-Aware Descriptions**
- **Movie cards**: Include rating, release date, and navigation hint
- **Navigation buttons**: Include destination context
- **Action buttons**: Include purpose and target
- **Status indicators**: Include current state and available actions

### 📱 Screen Reader Support

#### **TalkBack Integration**
- **Semantic roles** for all interactive elements
- **Content descriptions** for all visual elements
- **Navigation hints** for complex interactions
- **State announcements** for dynamic content

#### **VoiceOver Support**
- **iOS compatibility** for cross-platform accessibility
- **Consistent descriptions** across platforms
- **Gesture support** for navigation

### 🎯 Accessibility Best Practices

#### **Semantic Roles**
```kotlin
// Button role for interactive elements
.semantics {
    this.role = Role.Button
    this.contentDescription = "Retry loading movies"
}

// Card role for movie cards
.semantics {
    this.role = Role.Button
    this.contentDescription = "Movie: The Dark Knight, Rating: 8.5, Double tap to view details"
}
```

#### **Content Descriptions**
```kotlin
// Descriptive text for screen readers
ConfigurableText(
    text = "★ 8.5",
    contentDescription = "Rating: 8.5 out of 10"
)

// Action descriptions for buttons
ConfigurableButton(
    text = "Retry",
    contentDescription = "Retry loading movies"
)
```

#### **Navigation Hints**
```kotlin
// Clear navigation instructions
AccessibilityUtils.createMovieCardDescription(
    title = "The Dark Knight",
    rating = 8.5,
    releaseDate = "2008-07-18",
    isClickable = true // Adds "Double tap to view details"
)
```

### 🧪 Testing Accessibility

#### **Manual Testing**
1. **Enable TalkBack** on Android device
2. **Navigate through app** using screen reader
3. **Verify descriptions** are clear and helpful
4. **Test interactions** with screen reader enabled

#### **Automated Testing**
```kotlin
// Test semantic descriptions
@Test
fun `movie card has correct accessibility description`() {
    val movie = createTestMovie()
    val description = AccessibilityUtils.createMovieCardDescription(
        title = movie.title,
        rating = movie.rating,
        releaseDate = movie.releaseDate
    )
    assertTrue(description.contains(movie.title))
    assertTrue(description.contains("Double tap to view details"))
}
```

#### **Accessibility Testing Tools**
- **Android Accessibility Scanner**: Automated accessibility testing
- **TalkBack**: Screen reader testing
- **Accessibility Inspector**: iOS accessibility testing
- **axe-core**: Web accessibility testing (for web components)

### 📚 Accessibility Guidelines

#### **WCAG 2.1 Compliance**
- **Perceivable**: All content is accessible to screen readers
- **Operable**: All functionality is accessible via keyboard/screen reader
- **Understandable**: Clear descriptions and navigation hints
- **Robust**: Compatible with assistive technologies

#### **Android Accessibility Guidelines**
- **Semantic roles** for all interactive elements
- **Content descriptions** for all visual elements
- **Focus management** for navigation
- **State announcements** for dynamic content

#### **iOS Accessibility Guidelines**
- **VoiceOver support** for all elements
- **Dynamic type** support for text scaling
- **High contrast** support for visual accessibility
- **Switch control** support for motor accessibility

### 🔧 Implementation Examples

#### **Movie Card with Accessibility**
```kotlin
@Composable
fun MovieCard(movie: Movie, onClick: () -> Unit) {
    val description = AccessibilityUtils.createMovieCardDescription(
        title = movie.title,
        rating = movie.rating,
        releaseDate = movie.releaseDate
    )
    
    ConfigurableMovieCard(
        movie = movie,
        onClick = onClick,
        contentDescription = description
    )
}
```

#### **Button with Accessibility**
```kotlin
@Composable
fun RetryButton(onRetry: () -> Unit) {
    val description = AccessibilityUtils.createButtonDescription(
        action = "Retry",
        target = "loading movies"
    )
    
    ConfigurableButton(
        text = "Retry",
        onClick = onRetry,
        contentDescription = description
    )
}
```

#### **Loading State with Accessibility**
```kotlin
@Composable
fun LoadingScreen() {
    val description = AccessibilityUtils.createLoadingDescription("movie details")
    
    AccessibleLoadingIndicator(
        content = "movie details",
        modifier = Modifier.semantics {
            contentDescription = description
        }
    )
}
```

### 🎯 Future Enhancements

#### **Planned Features**
- **Voice commands** for navigation
- **Gesture recognition** for accessibility
- **Custom accessibility actions** for complex interactions
- **Accessibility preferences** for user customization

#### **Advanced Features**
- **Haptic feedback** for interactions
- **Audio descriptions** for movie content
- **Custom accessibility services** integration
- **Accessibility analytics** for usage tracking

---

**Last Updated**: 2024-01-24  
**Version**: 1.0.0  
**Status**: ✅ Implemented

## 📚 References

- [Android Accessibility Guidelines](https://developer.android.com/guide/topics/ui/accessibility)
- [WCAG 2.1 Guidelines](https://www.w3.org/WAI/WCAG21/quickref/)
- [Material Design Accessibility](https://material.io/design/usability/accessibility.html)
- [Jetpack Compose Accessibility](https://developer.android.com/jetpack/compose/accessibility)
