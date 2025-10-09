# QA Testing Guide

**Cinemy - QA Testing Guide**  
**Last Updated**: 2025-01-27  
**Version**: 3.0.0

> **📚 Layer Documentation**: For detailed testing of each layer, see:
> - [🗄️ Data Layer](./DATA_LAYER.md) - Data layer testing and MCP integration tests
> - [🤖 ML Layer](./ML_LAYER.md) - Machine learning testing and sentiment analysis tests
> - [🧭 Navigation Layer](./NAVIGATION_LAYER.md) - Navigation testing
> - [🎨 Presentation Layer](./PRESENTATION_LAYER.md) - ViewModel testing
> - [🖼️ UI Components Layer](./UI_COMPONENTS_LAYER.md) - UI component testing
> - [🔧 Utils Layer](./UTILS_LAYER.md) - Utility testing

## Overview

This guide provides comprehensive information for QA testing automation of the Cinemy app. The app includes extensive test tags, test IDs, and automation-friendly attributes to support various testing frameworks and tools.

## Test Automation Features

### 🏷️ Test Tags

The app uses consistent test tags for different UI components:

#### Navigation
- `nav_movies` - Movies navigation
- `nav_details` - Details navigation  
- `nav_back` - Back navigation

#### Movie List
- `movie_list` - Movie list container
- `movie_card` - Individual movie card
- `movie_title` - Movie title text
- `movie_rating` - Movie rating display
- `movie_poster` - Movie poster image
- `movie_release_date` - Release date text

#### Movie Details
- `movie_details_screen` - Movie details screen
- `movie_details_title` - Movie details title
- `movie_details_description` - Movie description
- `movie_details_rating` - Movie rating in details
- `movie_details_genres` - Movie genres
- `movie_details_runtime` - Movie runtime
- `movie_details_budget` - Movie budget
- `movie_details_revenue` - Movie revenue

#### Buttons
- `button_retry` - Retry button
- `button_back` - Back button
- `button_play` - Play button
- `button_load_more` - Load more button
- `button_previous` - Previous button
- `button_next` - Next button

#### Loading States
- `loading_indicator` - Loading spinner
- `loading_text` - Loading text
- `loading_movies` - Loading movies state
- `loading_details` - Loading details state

#### Error States
- `error_message` - Error message text
- `error_retry` - Error retry button
- `error_generic` - Generic error state
- `error_network` - Network error state

#### Pagination
- `pagination_info` - Pagination information
- `pagination_previous` - Previous page button
- `pagination_next` - Next page button
- `pagination_loading` - Pagination loading state

#### Sentiment Analysis
- `sentiment_analysis` - Sentiment analysis section
- `sentiment_positive` - Positive reviews
- `sentiment_negative` - Negative reviews
- `sentiment_loading` - Sentiment loading state

#### Foldable Device Support
- `foldable_layout` - Foldable device layout
- `dual_pane_left` - Left pane in dual pane mode
- `dual_pane_right` - Right pane in dual pane mode
- `adaptive_layout` - Adaptive layout container

#### Accessibility
- `accessible_text` - Accessible text elements
- `accessible_button` - Accessible button elements
- `accessible_card` - Accessible card elements
- `accessible_image` - Accessible image elements

### 🆔 Test IDs

Unique test IDs for specific elements:

#### Movie List
- `movie_list_id` - Movie list container
- `movie_card_id` - Individual movie card
- `movie_title_id` - Movie title
- `movie_rating_id` - Movie rating
- `movie_poster_id` - Movie poster

#### Movie Details
- `movie_details_id` - Movie details container
- `movie_details_title_id` - Movie details title
- `movie_details_description_id` - Movie description
- `movie_details_rating_id` - Movie rating

#### Buttons
- `button_retry_id` - Retry button
- `button_back_id` - Back button
- `button_play_id` - Play button

#### Loading
- `loading_indicator_id` - Loading spinner
- `loading_text_id` - Loading text

#### Error
- `error_message_id` - Error message
- `error_retry_id` - Error retry button

### 📊 Test Data Attributes

Data attributes for automation:

#### Movie Data
- `data-movie-id` - Movie ID
- `data-movie-title` - Movie title
- `data-movie-rating` - Movie rating
- `data-movie-release-date` - Release date
- `data-movie-runtime` - Runtime
- `data-movie-genres` - Genres
- `data-movie-budget` - Budget
- `data-movie-revenue` - Revenue

#### UI State
- `data-loading-state` - Loading state
- `data-error-state` - Error state
- `data-success-state` - Success state
- `data-empty-state` - Empty state

#### Pagination
- `data-current-page` - Current page
- `data-total-pages` - Total pages
- `data-has-next` - Has next page
- `data-has-previous` - Has previous page

#### Device Type
- `data-device-type` - Device type (phone/tablet/foldable/desktop)
- `data-screen-size` - Screen size
- `data-orientation` - Orientation (portrait/landscape)

## Testing Framework Integration

### Espresso Testing

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

### UI Automator

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

### Appium Testing

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

### Detox Testing

```javascript
// Find elements by test tag
await element(by.id('movie_card')).tap();
await element(by.id('button_retry')).tap();

// Find elements by test ID
await expect(element(by.id('movie_list_id'))).toBeVisible();
await expect(element(by.id('button_back_id'))).toBeVisible();
```

## Component Testing Examples

### ConfigurableText Testing

```kotlin
ConfigurableText(
    text = "Movie Title",
    testTag = TestUtils.TestTags.MOVIE_TITLE,
    testId = TestUtils.TestIds.MOVIE_TITLE_ID,
    testData = mapOf(
        TestUtils.TestData.MOVIE_TITLE to "The Dark Knight",
        TestUtils.TestData.MOVIE_RATING to "8.5"
    )
)
```

### ConfigurableButton Testing

```kotlin
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

### ConfigurableMovieCard Testing

```kotlin
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

## Test Scenarios

### 1. Movie List Testing

```kotlin
// Test movie list loading
onView(withTag("movie_list")).check(matches(isDisplayed()))
onView(withTag("loading_movies")).check(matches(isDisplayed()))

// Test movie card interaction
onView(withTag("movie_card")).perform(click())
onView(withTag("movie_details_screen")).check(matches(isDisplayed()))
```

### 2. Error Handling Testing

```kotlin
// Test error state
onView(withTag("error_message")).check(matches(isDisplayed()))
onView(withTag("button_retry")).perform(click())

// Test retry functionality
onView(withTag("loading_movies")).check(matches(isDisplayed()))
```

### 3. Pagination Testing

```kotlin
// Test pagination
onView(withTag("pagination_info")).check(matches(isDisplayed()))
onView(withTag("button_next")).perform(click())
onView(withTag("pagination_loading")).check(matches(isDisplayed()))
```

### 4. Foldable Device Testing

```kotlin
// Test foldable layout
onView(withTag("foldable_layout")).check(matches(isDisplayed()))
onView(withTag("dual_pane_left")).check(matches(isDisplayed()))
onView(withTag("dual_pane_right")).check(matches(isDisplayed()))
```

## Accessibility Testing

### Screen Reader Testing

```kotlin
// Test accessibility descriptions
onView(withContentDescription("Movie card for The Dark Knight. Rated 8.5 out of 10. Released on 2008-07-18."))
    .check(matches(isDisplayed()))

// Test button accessibility
onView(withContentDescription("Retry button")).perform(click())
```

### Voice Commands Testing

```kotlin
// Test voice commands
onView(withContentDescription("Movie card")).perform(click())
onView(withContentDescription("Back button")).perform(click())
```

## Performance Testing

### Load Testing

```kotlin
// Test loading states
onView(withTag("loading_movies")).check(matches(isDisplayed()))
onView(withTag("loading_details")).check(matches(isDisplayed()))

// Test loading completion
onView(withTag("movie_list")).check(matches(isDisplayed()))
onView(withTag("loading_movies")).check(matches(not(isDisplayed())))
```

### Memory Testing

```kotlin
// Test memory usage during navigation
onView(withTag("movie_card")).perform(click())
onView(withTag("button_back")).perform(click())
onView(withTag("movie_list")).check(matches(isDisplayed()))
```

## Best Practices

### 1. Test Tag Naming
- Use descriptive, consistent naming
- Follow the pattern: `component_type` (e.g., `movie_card`, `button_retry`)
- Use snake_case for multi-word tags

### 2. Test ID Management
- Use unique IDs for each element
- Follow the pattern: `component_type_id` (e.g., `movie_card_id`)
- Avoid conflicts with existing IDs

### 3. Test Data Attributes
- Use meaningful data attributes
- Follow the pattern: `data-attribute-name`
- Include relevant context information

### 4. Accessibility Testing
- Test with screen readers enabled
- Verify content descriptions are meaningful
- Test keyboard navigation
- Test voice commands

### 5. Cross-Platform Testing
- Test on different screen sizes
- Test on different orientations
- Test on foldable devices
- Test on different Android versions

## Troubleshooting

### Common Issues

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

### Debug Tips

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

## Conclusion

The Cinemy app provides comprehensive testing automation support through:

- **Test Tags**: Consistent naming for UI components
- **Test IDs**: Unique identifiers for specific elements
- **Test Data Attributes**: Contextual information for automation
- **Accessibility Support**: Screen reader and voice command compatibility
- **Cross-Platform Support**: Testing on different devices and orientations

This testing infrastructure enables comprehensive QA automation across various testing frameworks and tools, ensuring the app's quality and reliability.
